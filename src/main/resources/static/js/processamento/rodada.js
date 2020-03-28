$( function() {
	
	var partidasDaRodada = [];
	
	$("select[name='campeonato']").change(function() {
		var c = $(this).children("option:selected").val();

		$("#rodada").empty().append('<option value="0"></option>');
		
		if (c) {
			$.ajax({
				method: "GET",
				url: "/campeonatos/numeroDeRodadas",
				data: {
					idCampeonato: c
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
	});
	
	$("select[name='rodada']").change(function() {
		var rodadaSelecionada = $(this).children("option:selected").val();
		var campeonatoId = $("select[name='campeonato']").children("option:selected").val();
		
		listar(rodadaSelecionada, campeonatoId, partidasDaRodada);
	});
	
	$('#btnProcess').click(function() {
		$.ajax({
			method : "POST",
			url : "/processador/rodada/process",
			contentType:'application/json',
			dataType: "json",
			data : JSON.stringify(partidasDaRodada),
			success : function() {
				console.log('processado')
			}
		});
	});
});

function listar(rodada, campeonatoId, partidasDaRodada) {
	moment.locale('pt-br');
	$('.principal').attr('style', 'display:block');
	var $TABLE = $("#tblPartidasEncerradas");
	$.ajax({
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
			$('#btnProcess').attr('style', 'display:block');
		},
		statusCode: {
			404: function() {
				$TABLE.append('Não há partidas encerradas para esta rodada.')
				$TABLE.attr('style', 'display:block');
			}
		}
    });
}