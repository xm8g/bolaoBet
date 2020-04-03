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
			url : "/partidas/jogos/" + rodada,
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
						var palpitarBtnDisabled = '<td><button type="button" id="' + idBtnPalpite + '" class="btn btn-danger" disabled>Encerrado</button></td>';
						var botaoPalpite = expirouDataDoJogo(partida.data) ? palpitarBtnDisabled : palpitarBtn;
						
						var resultado = '';
						var rowResult = '';
						
						if(palpitePartida.pontosGanhos.pontos > -1) {
							resultado = traduzResultado(palpitePartida.pontosGanhos.motivoPonto, palpitePartida.pontosGanhos.pontos);
							rowResult = '<tr class="table-secondary"><td colspan="7">Resultado:   ' + extrairResultado(partida.resultado) + '</td></tr>';
						}
						
						var row = '<tr>' + mandante + golsMandante + '<td>X</td>' + golsVisitante + visitante + botaoPalpite + resultado + '</tr>';
						
						$('#tblPalpites').append(row);
						$('#tblPalpites').append(rowResult);
						
						$('#'+idBtnPalpite).on('click', function(){
							palpitar(partida, rodada, palpitePartida);
						});
					});
				} 
			},
			statusCode: {
				404: function() {
					bootbox.alert('Não há partidas liberadas para esta rodada.');
				}
			}
		})
	}
}

function traduzResultado(motivo, pontos) {
	//EXATO(18), GOLS_VENCEDOR(13), SALDO_GOLS(11), RESULTADO(9), EMPATE_GARANTIDO(5), RED(0);
	if (motivo == 'EXATO') {
		return '<td><img src="../image/icons/iconfinder_check_1930264.png" /> Cravou!<p>' + pontos + ' pontos.</p></td>'
	} else if (motivo == 'GOLS_VENCEDOR') {
		return '<td><img src="../image/icons/iconfinder_check_1930264.png" /> Gols do Vencedor!<p>' + pontos + ' pontos.</p></td>'
	} else if (motivo == 'SALDO_GOLS') {
		return '<td><img src="../image/icons/iconfinder_check_1930264.png" /> Saldo de Gols.<p>' + pontos + ' pontos.</p></td>'
	} else if (motivo == 'RESULTADO') {
		return '<td width="200px;"><img src="../image/icons/iconfinder_check_1930264.png" /> Acertou o Resultado!<p>' + pontos + ' pontos.</p></td>'
	} else if (motivo == 'EMPATE_GARANTIDO') {
		return '<td><img src="../image/icons/iconfinder_check_1930264.png" /> Empate Garantido!<p>' + pontos + ' pontos.</p></td>'
	} else if (motivo == 'RED') {
		return '<td><img src="../image/icons/iconfinder_sign-error_299045.png" /> RED!<p>' + pontos + ' ponto.</p></td>'
	}
	return '';
}

function extrairResultado(resultado) {
	var golsM = resultado.golsHTMandante + resultado.golsFTMandante;
	var golsV = resultado.golsHTVisitante + resultado.golsFTVisitante;
	
	return golsM + '  x  ' + golsV;
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