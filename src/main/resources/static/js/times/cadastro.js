$(document).ready(function() {
	var options = {
			url: '../../paises.json',

			getValue: 'name',

			list: {
				match: {
					enabled: true
				},
				maxNumberOfElements: 10
			},

			template: {
				type: 'custom',
				method: function (value, item) {
					return '<span class=\'flag flag-' + (item.code).toLowerCase() + '\' ></span>' + value;
				}
			}
		};

		$('#pais').easyAutocomplete(options);
});

function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#imgEscudo')
                .attr('src', e.target.result);
        };

        reader.readAsDataURL(input.files[0]);
    }
}