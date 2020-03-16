$(document).ready(function() {
	$("select[name='rodada']").change(function() {
		
		var rodada = $(this).children("option:selected").val();
		exibirTabelaDePartidas(rodada);
	});
	
	$('#btnSaveResult').click(function() {
		var rodada = $("select[name='rodada']").val()
		var palpite = {};
		
		palpite.golsMandante = $("input[name='golsMandante']").val();
		palpite.golsVisitante = $("input[name='golsVisitante']").val();
		palpite.partida = $("input[name='idPartida']").val();
		palpite.participante = $("input[name='participante']").val();
		if ($("input[name='id']").val()) {
			palpite.id = $("input[name='id']").val();
		}
		
		if (!palpite.golsMandante || !palpite.golsVisitante) {
			$('.feedback').empty().append('<small style="color:red">Voce deve informar o número de gols das duas equipes.</small>');
		} else {
			salvarPalpite(palpite, rodada);
		}
	}); 
});

function salvarPalpite(palpite, rodada) {
	$.ajax({
		method : "POST",
		url : "/palpites/salvar",
		data : palpite,
		success : function() {
			$('#dialog-palpite').modal('hide');
			exibirTabelaDePartidas(rodada);
		},
		statusCode: {
			422: function(xhr) {
				console.log('status error:', xhr.status);
				var errors = $.parseJSON(xhr.responseText);
				var msgErro = '';
				$.each(errors, function(key, val) {
					msgErro= msgErro + val + '\n';
				})
				bootbox.alert(msgErro);
			}
		}
	})
}

function exibirTabelaDePartidas(rodada) {
	if (rodada) {
		$('#tblPalpites').attr('style', 'display:block');
		$('#tblPalpites').empty();
		$.ajax({
			method : "GET",
			url : "/partidas/tabela/listagem/" + rodada,
			success : function(partidas) {
				if (partidas) {
					$.each(partidas, function( i, partida ) {
						var palpitePartida = obterPalpiteJaCadastradoDaPartida(partida);
						var idBtnPalpite = 'btnPalpite' + partida.id;
						var mandante = '<td align="right">' + partida.mandante.nome + '  <img width="28" height="28" src="data:image/png;base64,'+partida.mandante.escudo.data+'" /></td>';
						var golsMandante = '<td></td>';
						var golsVisitante = '<td></td>';
						if (palpitePartida) {
							golsMandante = '<td>' + palpitePartida.golsMandante + '</td>';
							golsVisitante = '<td>' + palpitePartida.golsVisitante + '</td>';
						}
						var visitante = '<td><img width="28" height="28" src="data:image/png;base64,'+partida.visitante.escudo.data+'" />  ' + partida.visitante.nome + '</td>';
						var palpitarBtn = '<td><button type="button" id="' + idBtnPalpite + '" class="btn btn-success">Palpite</button></td>';
						var palpitarBtnDisabled = '<td><button type="button" id="' + idBtnPalpite + '" class="btn btn-danger" disabled>Expirou</button></td>';
						var botaoPalpite = expirouDataDoJogo(partida.data) ? palpitarBtnDisabled : palpitarBtn;  
						var partidaVencida = '<td></td>';
						var row = '<tr>' + mandante + golsMandante + '<td>X</td>' + golsVisitante + visitante + botaoPalpite + '</tr>';
						$('#tblPalpites').append(row);
						$('#'+idBtnPalpite).on('click', function(){
							palpitar(partida, rodada, palpitePartida);
						});
					});
				}
			}
		})
	}
}

function obterPalpiteJaCadastradoDaPartida(partida) {
	var palpite = {}
	$.ajax({
		method : "GET",
		url : "/palpites/partida",
		data : {
			partidaId: partida.id,		
		},
		success : function(response) {
			palpite = response;
		},
		statusCode: {
			404: function(xhr) {
				palpite.golsMandante = '';
				palpite.golsVisitante = '';
			}
		},
		async: false
		
	})
	return palpite;
}

function expirouDataDoJogo(data) {
	var dataPartida = new Date(data);
	var agora = new Date();
	
	return dataPartida < agora;
}

function palpitar(partida, rodada, palpitePartida) {
	console.log(partida.id, rodada, palpitePartida.id)
	moment.locale('pt-br');
	
	if (typeof palpitePartida.golsMandante == 'number') {
		$("input[name='golsMandante']").val(palpitePartida.golsMandante);
	} else {
		$("input[name='golsMandante']").val('');
	} 
	if (typeof palpitePartida.golsVisitante == 'number') {
		$("input[name='golsVisitante']").val(palpitePartida.golsVisitante);
	} else {
		$("input[name='golsVisitante']").val('');
	}
	if (typeof palpitePartida.id == 'number') {
		$('#id').val(palpitePartida.id);
	} else {
		$('#id').val('');
	}
		
	$('.escudoM').empty().append(partida.mandante.nome + '   <img width="32" height="32" src="data:image/png;base64,'+partida.mandante.escudo.data+'" />');
	$('.escudoV').empty().append('<img width="32" height="32" src="data:image/png;base64,'+partida.visitante.escudo.data+'" />   ' + partida.visitante.nome);
	$('#idPartida').val(partida.id);
	$('.localData').empty().append(partida.local + ' ' + moment(partida.data).format('LLL'));
	
	$('#dialog-palpite').modal('show')
}