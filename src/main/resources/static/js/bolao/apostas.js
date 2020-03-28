$(document).ready(function() {
	$("select[name='rodada']").change(function() {
		console.log('324324324234324')
		var rodada = $(this).children("option:selected").val();
		exibirTabelaDePartidas(rodada);
	});
	
	
});

function exibirTabelaDePartidas(rodada) {
	if (rodada) {
		$('#tblPartidas').attr('style', 'display:block');
		$('#tblPartidas').empty();
		$.ajax({
			method : "GET",
			url : "/partidas/jogos/" + rodada,
			success : function(partidas) {
				if (partidas) {
					$.each(partidas, function( i, partida ) {
						var idBtnPalpite = 'btnPalpite' + partida.id;
						var confronto = '<td align="center" style="padding-top: 15px;">' + partida.mandante.nome + '  <img width="16" height="16" src="data:image/png;base64,'+partida.mandante.escudo.data+'" />' + 
										' X <img width="16" height="16" src="data:image/png;base64,'+partida.visitante.escudo.data+'" />  ' + partida.visitante.nome + '</td>';
						var palpitarBtn = '<td><button type="button" id="' + idBtnPalpite + '" class="btn btn-success">Apostar</button></td>';
						var palpitarBtnDisabled = '<td><button type="button" id="' + idBtnPalpite + '" class="btn btn-danger" disabled>Expirou</button></td>';
						var botaoMercados = expirouDataDoJogo(partida.data) ? palpitarBtnDisabled : palpitarBtn;  
						var partidaVencida = '<td></td>';
						var row = '<tr>' + confronto + botaoMercados + '</tr>';
						$('#tblPartidas').append(row);
						$('#'+idBtnPalpite).on('click', function(){
							
						});
					});
				}
			},
			,
			statusCode: {
				404: function() {
					bootbox.alert('Não há partidas liberadas para esta rodada.');
				}
			}
		})
	}
}

function expirouDataDoJogo(data) {
	var dataPartida = new Date(data);
	var agora = new Date();
	
	return dataPartida < agora;
}