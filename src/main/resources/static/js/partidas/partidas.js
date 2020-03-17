$( function() {
	
	$("select[name='campeonato']").change(function() {
		var c = $(this).children("option:selected").val();

		$("#cardTimes").empty()
		$("#cardLocais").empty();
		$("#rodada").empty().append('<option value="0"></option>');
		$('.principal').attr('style', 'display: none;');
		$('.principal').attr('style', 'display: block; font-size: 12px;');
		
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
		$("#colRodada").empty().append('Rodada ' + rodadaSelecionada);
		var campeonatoId = $("select[name='campeonato']").children("option:selected").val();
		carregaTimes(campeonatoId);
		listar(rodadaSelecionada, campeonatoId);
	});
	$("input[name='data']").change(function(e) {
		$("#error-data").empty();
	});
	
	
	$('.localPartida').droppable({
		accept: '.local',
		classes: {
	        "ui-droppable-active": "ui-state-highlight"
	    },
		drop: function(event, ui){
			ui.draggable.addClass('dropped');
			$(this).empty();
			$(this).append(ui.draggable.html())
			ui.draggable.detach();
			$('.localPartida').popover('hide');
		}
	});
	$('.casa').droppable({
		accept: '.time',
		classes: {
	        "ui-droppable-active": "ui-state-highlight"
	    },
		drop: function(event, ui){
			ui.draggable.addClass('dropped');
			var props = transferData(ui.draggable);
			$(this).empty();
			$(this).attr('data-mandante', props.id)
			$(this).append(props.content);
			ui.draggable.detach();
			$("#card-mandante").popover('hide');
		}
	});
	$('.fora').droppable({
		accept: '.time',
		classes: {
	        "ui-droppable-active": "ui-state-highlight"
	    },
		drop: function(event, ui){
			ui.draggable.addClass('dropped');
			var props = transferData(ui.draggable);
			$(this).empty();
			$(this).attr('data-visitante', props.id)
			$(this).append(props.content);
			ui.draggable.detach();
			$("#card-visitante").popover('hide');
		}
	});
	
	$('#btnCleanCard').click(function() {
		rollbackDragDropTimeCasa();
		rollbackDragDropTimeFora();
		rollbackDragDropLocalPartida();
		$("input[name='data']").val('');
	});
	
	$("#partidas-add").submit(function(evt) {
		//bloquear o comportamento padrão do submit
		evt.preventDefault();
		
		var partida = {};
		partida.campeonato = $( "select[name='campeonato'] option:selected" ).val();
		partida.rodada = $( "select[name='rodada'] option:selected").val();
		partida.mandante = $("#card-mandante").attr('data-mandante') 
		partida.visitante = $("#card-visitante").attr('data-visitante')
		partida.local = $('.localPartida').html();
		partida.data = $("input[name='data']").val();
		
		console.log('partida > ', JSON.stringify(partida));
		
		$.ajax({
			type: "POST",
			url: "/partidas/salvar",
			data: partida,
			success: function() {
				clearForm();
				listar(partida.rodada, partida.campeonato);
			},
			statusCode: {
				422: function(xhr) {
					console.log('status error:', xhr.status);
					var errors = $.parseJSON(xhr.responseText);
					$.each(errors, function(key, val) {
						exibeErro(key, val);
					})
				}
			},
			error: function(xhr) {
				console.log("> error: ", xhr.responseText);
				$("#alert").addClass("alert alert-danger").text("Não foi possível salvar esta partida.");
			}
		});
	});
	
	$('#btnSaveResult').click(function() {
		var resultado = {};
		var golsHTCasa = parseInt($("input[name='golsHTMandante']").val());
		var golsFTCasa = parseInt($("input[name='golsFTMandante']").val());
		var golsHTFora = parseInt($("input[name='golsHTVisitante']").val());
		var golsFTFora = parseInt($("input[name='golsFTVisitante']").val());
		var id = $('#idPartida').val();
		var rodada = $( "select[name='rodada'] option:selected").val();
		
		if (isNaN(golsHTCasa)) {
			golsHTCasa = 0;
		}
		if (isNaN(golsFTCasa)) {
			golsFTCasa = 0;
		}
		if (isNaN(golsHTFora)) {
			golsHTFora = 0;
		}
		if (isNaN(golsFTFora)) {
			golsFTFora = 0;
		}
		
		resultado.golsHTMandante = golsHTCasa;
		resultado.golsHTVisitante = golsHTFora;
		resultado.golsFTMandante = golsFTCasa;
		resultado.golsFTVisitante = golsFTFora;
		var campeonatoId = $("select[name='campeonato']").children("option:selected").val();
		$.ajax({
			method: "POST",
			url : "/resultados/resultado/"+id,
			data : resultado,
			success: function() {
				$('#dialog-resultado').modal('hide')
				listar(rodada, campeonatoId);
			}
	    });

		
	});
	
});

function rollbackDragDropTimeCasa() {
	var htmlDivTime = $('#card-mandante').html();
	var idTime = $('#card-mandante').attr('data-mandante');
	if (idTime) {
		$('#card-mandante').attr('data-mandante', '');
		$('#card-mandante').empty();
		$('#card-mandante').append('<small>Arraste e solte o time da casa aqui</small>')
		var divTime = '<div class="time m-1 pl-3" data-id="'+ idTime +'">'+ htmlDivTime +'</div>';
		
		$("#cardTimes").append(divTime).fadeIn();
		tornarDivDoTimeArrastavel();
	}
}

function exibeErro(field, msg) {
	console.log(field, msg);
	if (field == 'mandante') {
		$("#card-mandante").attr('data-content', msg)
		$("#card-mandante").popover('show');
	} else if (field == 'visitante') {
		$("#card-visitante").attr('data-content', msg)
		$("#card-visitante").popover('show');
	} else if (field == 'local') {
		$(".localPartida").attr('data-content', msg)
		$(".localPartida").popover('show');
	} else if (field == 'data') {
		$("#error-data").append("<span style='color:red'>" + msg + "</span>");
	} else if (field == 'rodada') {
		
	}
}

function clearForm() {
	$('#card-mandante').attr('data-mandante', '');
	$('#card-mandante').empty();
	$('#card-mandante').append('<small>Arraste e solte o time da casa aqui</small>')
	$('#card-visitante').attr('data-visitante', '');
	$('#card-visitante').empty();
	$('#card-visitante').append('<small>Arraste e solte o time visitante aqui</small>');
	$('.localPartida').empty();
	$('.localPartida').append('<small>Arraste e solte o local da partida aqui</small>');
	$("#alert").removeClass("alert").empty();
}

function rollbackDragDropTimeFora() {
	var htmlDivTime = $('#card-visitante').html();
	var idTime = $('#card-visitante').attr('data-visitante');
	if (idTime) {
		$('#card-visitante').attr('data-visitante', '');
		$('#card-visitante').empty();
		$('#card-visitante').append('<small>Arraste e solte o time visitante aqui</small>')
		var divTime = '<div class="time m-1 pl-3" data-id="'+ idTime +'">'+ htmlDivTime +'</div>';
		
		$("#cardTimes").append(divTime).fadeIn();
		tornarDivDoTimeArrastavel();
	}
}

function rollbackDragDropLocalPartida() {
	var htmlDivLocalPartida = $('.localPartida').html();
	$('.localPartida').empty();
	$('.localPartida').append('<small>Arraste e solte o local da partida aqui</small>')
	var divLocalPartida = '<div class="local m-1">'+ htmlDivLocalPartida +'</div>';
	$("#cardLocais").append(divLocalPartida);
	tornarDivDoLocalArrastavel();
}

function tornarDivDoTimeArrastavel() {
	$('.time').draggable({
		revert: "invalid", //se não soltar no lugar certo, volta pro início
		containment: "window",
		cursor: "move",
		start: function(event, ui){
			$(this).addClass('dragged');
		},
		stop: function(event, ui){
			$(this).removeClass('dragged');
		}
	});
}

function tornarDivDoLocalArrastavel() {
	$('.local').draggable({
		revert: "invalid", //se não soltar no lugar certo, volta pro início
		containment: "window",
		cursor: "move",
		start: function(event, ui){
			$(this).addClass('dragged');
		},
		stop: function(event, ui){
			$(this).removeClass('dragged');
		}
	});
}

function transferData($item) {
	var props = {
		id: '',
		content: ''
	};
	props.id = $item.attr('data-id');
	props.content = $item.html();
	return props;
}

function carregaTimes($idCampeonato) {
	$('#cardTimes').empty();
	$('#cardLocais').empty();
	$.ajax({
		method: "GET",
		url: "/campeonatos/times",
		data: {
			idCampeonato: $idCampeonato
        },
		success: function( times ) {
			for (i=0; i < times.length; i++) {
				var time = times[i];
				var divTime = '<div class="time m-1 pl-3" data-id="'+ time.id +'"><img width="16" height="16" src="data:image/png;base64,'+time.escudo.data+'" /><span class="ml-1">'+ time.nome +'</span></div>';
				var divLocal = '<div class="local m-1">'+ time.casa +'</div>';
				$("#cardTimes").append(divTime);
				$("#cardLocais").append(divLocal);
			}
			tornarDivDoTimeArrastavel();
			$('.local').draggable({
				revert: "invalid", //se não soltar no lugar certo, volta pro início
				containment: "window",
				cursor: "move",
				start: function(event, ui){
					$(this).addClass('dragged');
				},
				stop: function(event, ui){
					$(this).removeClass('dragged');
				}
			});
		} 
	})
}

function listar(rodada, campeonatoId) {
	moment.locale('pt-br');
	$.ajax({
		method: "GET",
		url : "/partidas/tabela/listagem/"+rodada+"/"+campeonatoId,
		success: function( partidas ) {
			var $TABLE = $("#tblPartidasCadastradas");
			$TABLE.empty();
			var header = '<thead><tr><th>Confronto</th><th>Local</th><th>Data</th><th>Gols</th><th>Apagar</th></tr></thead>';
			$TABLE.append(header);
			if (partidas.length > 0) {
				$TABLE.append('<tbody>');
				partidas.forEach(function( partida, index ) {
					var golsCasa = '';
					var golsFora = '';
					var idTdEditPartida = 'tdEdit' + partida.id;
					if (partida.resultado) {
						golsCasa = parseInt(partida.resultado.golsHTMandante) + parseInt(partida.resultado.golsFTMandante);
						golsFora = parseInt(partida.resultado.golsHTVisitante) + parseInt(partida.resultado.golsFTVisitante); 
					}
					var $row = '<tr><td class="align-middle">' + 
							partida.mandante.nome + ' <img width="16" height="16" src="data:image/png;base64,'+partida.mandante.escudo.data+'" />  ' + golsCasa + ' X ' + golsFora +  
							'  <img width="16" height="16" src="data:image/png;base64,'+partida.visitante.escudo.data+'" /> ' + partida.visitante.nome +
							'</td><td class="align-middle">' + partida.local + '</td><td class="align-middle">' + moment(partida.data).format('lll') + '</td>' +
							'<td id="' + idTdEditPartida + '"><a class="btn btn-secondary btn-sm btn-block" href="javascript:void(0)"><i class="fas fa-futbol"></i></a></td>' +
							'<td><a class="btn btn-danger btn-sm btn-block" href="#" onClick="deletePartida(' + partida.id +','+ rodada +');" role="button"><i class="fas fa-times-circle"></i></a></td>' +
							'</tr>';
					$TABLE.append($row);
					retirarArrastaveisDasColunasDeSelecao(partida.mandante.id, partida.visitante.id, partida.local);
					$('#'+idTdEditPartida).on('click', function(){
						resultado(partida, rodada);
					});
				});
				$TABLE.append('</tbody>');
				$TABLE.attr('style', 'display:block');
			} else {
				$TABLE.append('Não há partidas cadastradas para esta rodada.')
			}
		}
    });
}

function resultado(partida, rodada) {
	$('#dialog-resultado').modal('show')
	$('.escudoM').empty().append(partida.mandante.nome + '   <img width="32" height="32" src="data:image/png;base64,'+partida.mandante.escudo.data+'" />');
	$('.escudoV').empty().append('<img width="32" height="32" src="data:image/png;base64,'+partida.visitante.escudo.data+'" />   ' + partida.visitante.nome);
	$('#idPartida').val(partida.id);
	
	if (partida.resultado) {
		$("input[name='golsHTMandante']").val(partida.resultado.golsHTMandante);
		$("input[name='golsFTMandante']").val(partida.resultado.golsFTMandante);
		$("input[name='golsHTVisitante']").val(partida.resultado.golsHTVisitante);
		$("input[name='golsFTVisitante']").val(partida.resultado.golsFTVisitante);
	} else {
		$("input[name='golsHTMandante']").val('');
		$("input[name='golsFTMandante']").val('');
		$("input[name='golsHTVisitante']").val('');
		$("input[name='golsFTVisitante']").val('');	
	}
	
}

function deletePartida(id, rodada) {
	var campeonato = $("select[name='campeonato']").children("option:selected").val();
	$.ajax({
		method: "GET",
		url : "/partidas/delete/"+id,
		complete: function() {
			carregaTimes(campeonato);
			listar(rodada);
		}
    });
}

function retirarArrastaveisDasColunasDeSelecao(mandante, visitante, local) {
	$(".time").each(function() {
		if ($(this).attr('data-id') == mandante || $(this).attr('data-id') == visitante) {
			$(this).remove();
		}
	});
	$(".local").each(function() {
		if (local == $(this).html()) {
			$(this).remove();
		}
	});
}

function golsDaPartida() {
	var golsHTCasa = parseInt($("input[name='golsHTMandante']").val());
	var golsFTCasa = parseInt($("input[name='golsFTMandante']").val());
	var golsHTFora = parseInt($("input[name='golsHTVisitante']").val());
	var golsFTFora = parseInt($("input[name='golsFTVisitante']").val());
	
	if (isNaN(golsHTCasa)) {
		golsHTCasa = 0;
	}
	if (isNaN(golsFTCasa)) {
		golsFTCasa = 0;
	}
	if (isNaN(golsHTFora)) {
		golsHTFora = 0;
	}
	if (isNaN(golsFTFora)) {
		golsFTFora = 0;
	}
	var golsCasa = Math.abs(golsHTCasa) + Math.abs(golsFTCasa);
	var golsFora = Math.abs(golsHTFora) + Math.abs(golsFTFora);
	
	$('#placarFinal').empty().append('Resultado Final:  ' + golsCasa + ' x ' + golsFora);
}
