$(document).ready(function() {
	
	$("select[name='campeonato']").change(function() {
		var c = $(this).children("option:selected").val();
		if (!c) {
			$("#cardTimes").empty()
			$("#cardLocais").empty();
			$('#rodada > option').each(function() {
			    if ( $(this).val() !== '' ) {
			        $(this).remove();
			    }
			});
			$('.principal').attr('style', 'display: none;');
			return;
		}
		$('.principal').attr('style', 'display: block; font-size: 12px;');
		$.ajax({
			method: "GET",
			url: "/campeonatos/numeroDeTimes",
			data: {
				idCampeonato: c
	        },
			success: function( response ) {
				if (response) {
					for(i=0; i < response; i++) {
						var valor = i+1;
						$("#rodada").append(new Option(valor, valor));
					}
					carregaTimes(c);
				}
			} 
		})
	});
	
	$("select[name='rodada']").change(function() {
		var c = $(this).children("option:selected").val();
		$("#colRodada").empty().append('Rodada ' + c);
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
				//chamar lista
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
			},
			complete: function() {
				//carregar a lista de jogos salvos
				console.log('complete');
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
