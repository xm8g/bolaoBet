$( function() {
	
	var partidasDaRodada = [];
	
	$("select[name='campeonato']").change(function() {
		var c = $(this).children("option:selected").val();

		$("#rodada").empty().append('<option value="0"></option>');
		
		listarRodadasByCampeonato(c);
	});
	
	$("select[name='rodada']").change(function() {
		var rodadaSelecionada = $(this).children("option:selected").val();
		var campeonatoId = $("select[name='campeonato']").children("option:selected").val();
		
		listar(rodadaSelecionada, campeonatoId, partidasDaRodada);
	});
	
	$('#btnProcess').click(function() {
		$(this).addClass("running");
		$.ajax({
			async: false,
			method : "POST",
			url : "/processador/rodada/process",
			contentType:'application/json',
			dataType: "json",
			data : JSON.stringify(partidasDaRodada),
			success : function() {
				console.log('processados palpites')
			}
		});
		var campeonatoId = $("select[name='campeonato']").children("option:selected").val();
		var rodada = $("select[name='rodada']").children("option:selected").val();
		atualizarClassificacaoDosBoloesComEsteCampeonato(campeonatoId, rodada)
	});
});

function listarRodadasByCampeonato(campeonatoId) {
	if (campeonatoId) {
		$.ajax({
			async: false,
			method: "GET",
			url: "/campeonatos/numeroDeRodadas",
			data: {
				idCampeonato: campeonatoId
	        },
			success: function( response ) {
				if (response) {
					for(i=0; i < response; i++) {
						var valor = i+1;
						$("#rodada").append(new Option(valor, valor));
					}
				}
			} 
		})
	}
}

function listar(rodada, campeonatoId, partidasDaRodada) {
	moment.locale('pt-br');
	var rodadaPendente = false;
	$('.principal').attr('style', 'display:block');
	var $TABLE = $("#tblPartidasEncerradas");
	$.ajax({
		async: false,
		method: "GET",
		url : "/processador/partidasEncerradas/"+rodada+"/"+campeonatoId,
		success: function( partidas ) {
			$TABLE.empty();
			$TABLE.append('<tbody>');
			partidas.forEach(function( partida, index ) {
				partidasDaRodada.push(partida);
				var golsCasa = '';
				var golsFora = '';
				if (partida.resultado) {
					golsCasa = parseInt(partida.resultado.golsHTMandante) + parseInt(partida.resultado.golsFTMandante);
					golsFora = parseInt(partida.resultado.golsHTVisitante) + parseInt(partida.resultado.golsFTVisitante); 
				}
				var $row = '<tr><td class="align-middle" align="right">' + 
						partida.mandante.nome + ' <img width="16" height="16" src="data:image/png;base64,'+partida.mandante.escudo.data+'" /></td><td>  ' + golsCasa + ' X ' + golsFora + '</td>' +  
						'<td align="left"><img width="16" height="16" src="data:image/png;base64,'+partida.visitante.escudo.data+'" /> ' + partida.visitante.nome +
						'</td><td class="align-middle">' + partida.local + '</td><td class="align-middle">' + moment(partida.data).format('lll') + '</td>' +
						'</tr>';
				$TABLE.append($row);
			});
			$TABLE.append('</tbody>');
			$TABLE.attr('style', 'display:block');
			$('#btnProcess').attr('style', 'display:block;');
		},
		statusCode: {
			404: function() {
				$TABLE.append('Não há partidas encerradas para esta rodada.')
				$TABLE.attr('style', 'display:block');
				rodadaPendente = true;
			}
		}
    });
	
	if (rodadaPendente) return;
	
	$.ajax({
		method: "GET",
		url : "/processador/isClassificacaoEncerrada/"+rodada+"/"+campeonatoId,
		success: function() {
			$('#divAlertRodadaEncerrada').attr('style', 'display:block');
			$('#divAlertRodadaEncerrada').empty().append('Esta rodada já foi processada e classificada! Se deseja voltar atrás clique no botão ao lado   ' +
					'<a href="/classificacao/rollback/' + rodada + '/' + campeonatoId + '" class="btn btn-warning"><i class="fas fa-undo"></i>&nbsp;&nbsp;Rollback</a>')
			$('#btnProcess').attr('disabled', true);
		},
		statusCode: {
			422: function() {
				$('#divAlertRodadaEncerrada').attr('style', 'display:block');
				$('#divAlertRodadaEncerrada').empty().append('Não há bolões criados para este campeonato!')
				$('#btnProcess').attr('style', 'display:none');
			}
		},
		statusCode: {
			404: function() {
				$('#divAlertRodadaEncerrada').attr('style', 'display:none');
				$('#btnProcess').attr('style', 'display:block');
				$('#btnProcess').attr('disabled', false);
			}
		}
    });
	
}

function atualizarClassificacaoDosBoloesComEsteCampeonato(campeonatoId, rodada) {
	$.ajax({
		method : "GET",
		url : "/processador/classificacao/process/" + campeonatoId + "/" + rodada,
		success : function() {
			$('#btnProcess').removeClass("running");
		}
	});
}