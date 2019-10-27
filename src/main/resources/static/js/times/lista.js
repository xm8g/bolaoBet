$(document).ready(function() {
    var table = $('#table-times').DataTable({
    	language: {
           url: '../../Portuguese-Brasil.json',
        },
        searching : true,
        lengthMenu : [ 10, 10 ],
        processing : true,
        serverSide : true,
        responsive : true,
        order: [2, 'desc'],
        ajax : {
            url : "/times/tabela/listagem",
            data : "data"
        },
        columnDefs: [
        	{ width: "5%", className: "text-center", targets: 0 },
        	{ width: "8%", className: "text-center", targets: 1 },
        	{ width: "25%", targets: 2 },
        	{ width: "25%", targets: 3 },
        	{ width: "25%", targets: 4 },
        	{ width: "4%", className: "text-center", targets: 5 },
        	{ width: "4%", className: "text-center", targets: 6 },
        ],
        columns : [
            {data : 'id'},
            {orderable : false, data: 'escudo.data', "render" : function(img) {
            		return '<img src="data:image/png;base64,'+img+'" />';	
            	}	
            },
            {data : 'nome'},
            {data : 'pais'},
            {data : 'casa'},
            {orderable : false,	data : 'id', "render" : function(id) {
                    return '<a class="btn btn-success btn-sm btn-block" href="/times/editar/'
                            + id + '" role="button"><i class="fas fa-edit"></i></a>';
                }
            },
            {orderable : false,	data : 'id', "render" : function(id) {
                    return '<a class="btn btn-danger btn-sm btn-block" href="/times/excluir/'
                    + id +'" role="button" data-toggle="modal" data-target="#confirm-modal"><i class="fas fa-times-circle"></i></a>';
                }
            }
        ]
    });
});