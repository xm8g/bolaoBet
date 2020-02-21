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
		}
	});
	
	$('#btnCleanCard').click(function() {
		rollbackDragDropTimeCasa();
		rollbackDragDropTimeFora();
		rollbackDragDropLocalPartida();
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
	var htmlDivTime = $('.localPartida').html();
	/* se não contém <small> pegar o texto e criar a div local dragble */
	
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


//function salvarPartidaAtual() {
//	var partida = {};
//	partida.campeonato = $( "select[name='campeonato'] option:selected" )).val();
//	partida.rodada = $("input[name='rodada']").val();
//	partida.mandante = $( "select[name='mandante'] option:selected" )).val();
//	partida.visitante = $( "select[name='visitante'] option:selected" )).val();
//	partida.local = $("input[name='local']").val();
//	partida.dataHora = $("input[name='data']").val();
//	
//	console.log('partida > ', partida);
//}