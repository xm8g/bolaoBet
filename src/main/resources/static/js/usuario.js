$(document).ready(function() {
	$('.pass').keyup(function() {
		$('#senha1').val() === $('#senha2').val()
		? $('#btnTrocaSenha').prop('disabled', false)
		: $('#btnTrocaSenha').prop('disabled', true);
	})
	
});	