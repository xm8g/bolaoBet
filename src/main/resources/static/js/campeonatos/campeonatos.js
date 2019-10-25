function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#imgEscudo').attr('src', e.target.result);
        };

        reader.readAsDataURL(input.files[0]);
    }
}

$(document).ready(function() {
    var table = $('#table-campeonatos').DataTable({
    	language: {
           url: '../../Portuguese-Brasil.json',
        },
        searching : true,
        lengthMenu : [ 5, 10 ],
        processing : true,
        serverSide : true,
        responsive : true,
        order: [2, 'desc'],
        ajax : {
            url : "/campeonatos/tabela/listagem",
            data : "data"
        },
        columnDefs: [
        	{ width: "5%", className: "text-center", targets: 0 },
        	{ width: "8%", className: "text-center", targets: 1 },
        	{ width: "35%", targets: 2 },
        	{ width: "35%", targets: 3 },
        	{ width: "4%", className: "text-center", targets: 4 },
        	{ width: "4%", className: "text-center", targets: 5 },
        ],
        columns : [
            {data : 'id'},
            {orderable : false, data: 'escudo.data', "render" : function(img) {
            		return '<img src="data:image/png;base64,'+img+'" width="32" height="32" />';	
            	}	
            },
            {data : 'nome'},
            {data : 'rodadas'},
            {orderable : false,	data : 'id', "render" : function(id) {
                    return '<a class="btn btn-success btn-sm btn-block" href="/campeonatos/editar/'
                            + id + '" role="button"><i class="fas fa-edit"></i></a>';
                }
            },
            {orderable : false,	data : 'id', "render" : function(id) {
                    return '<a class="btn btn-danger btn-sm btn-block" href="/campeonatos/excluir/'
                    + id +'" role="button" data-toggle="modal" data-target="#confirm-modal"><i class="fas fa-times-circle"></i></a>';
                }
            }
        ]
    });
});