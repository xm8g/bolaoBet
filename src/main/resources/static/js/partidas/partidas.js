$(document).ready(function() {
	
	$("select[name='campeonato']").change(function() {
		var c = $(this).children("option:selected").val();
		$.ajax({
			method: "GET",
			url: "/campeonatos/numeroDeTimes",
			data: {
				idCampeonato: c
	        },
			success: function( response ) {
	            if (response.length > 100) {
	                $('.row').append( $(response).hide().fadeIn(400) );
	            } else {   		
	                $("#fim-btn").show();
	                $("#loader-img").removeClass("loader");
	            }
			}, 
			error: function(xhr) {
				alert("Ops, ocorreu um erro: " + 
				       xhr.status + " - " + xhr.statusText);
	        },
	        complete: function() {
	            $("#loader-img").hide();
	        }
		})
	})
	
	function setClasses(index, steps) {
		if (index < 0 || index > steps)
			return;
		if (index == 0) {
			$("#prev").prop('disabled', true);
		} else {
			$("#prev").prop('disabled', false);
		}
		if (index == steps) {
			$("#next").text('Rodada Cadastrada');
		} else {
			$("#next").text('Próxima Partida');
		}
		$(".ul-wizard li").each(function() {
			$(this).removeClass();
		});
		$(".ul-wizard li:lt(" + index + ")").each(function() {
			$(this).addClass("done");
		});
		$(".ul-wizard li:eq(" + index + ")").addClass("active")
		var p = index * (100 / steps);
		$("#progresso").width(p + '%');
	}
	$(".step-wizard .ul-wizard a").click(function() {
		var step = $(this).find("span.step")[0].innerText;
		var steps = $(".step-wizard .ul-wizard li").length;
		setClasses(step - 1, steps - 1)
	});
	$("#btnProximaPartida").click(function() {
		salvarPartidaAtual();
		var step = $(".step-wizard .ul-wizard li.active span.step")[0].innerText;
		var steps = $(".step-wizard .ul-wizard li").length;
		setClasses(step - 2, steps - 1);
	});
	$("#btnPartidaAnterior").click(function() {
		var step = $(".step-wizard ul li.active span.step")[0].innerText;
		var steps = $(".step-wizard ul li").length;
		setClasses(step, steps - 1);
	});

	// initial state setup
	setClasses(0, $(".step-wizard .ul-wizard li").length);
});

function salvarPartidaAtual() {
	var partida = {};
	partida.campeonato = $( "select[name='campeonato'] option:selected" )).val();
	partida.rodada = $("input[name='rodada']").val();
	partida.mandante = $( "select[name='mandante'] option:selected" )).val();
	partida.visitante = $( "select[name='visitante'] option:selected" )).val();
	partida.local = $("input[name='local']").val();
	partida.dataHora = $("input[name='data']").val();
	
	console.log('partida > ', partida);
}