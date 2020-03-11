$(document).ready(function() {
	
	
    var table = $('#table-boloes').DataTable({
    	language: {
           url: '../../Portuguese-Brasil.json',
        },
        searching : true,
        lengthMenu : [ 5, 10 ],
        processing : true,
        serverSide : true,
        responsive : true,
        order: [1, 'desc'],
        ajax : {
            url : "/bolao/tabela/listagem",
            data : "data"
        },
        columnDefs: [
        	{ width: "5%", className: "text-center", targets: 0 },
        	{ width: "90%", className: "text-center", targets: 1 },
        	{ width: "5%", className: "text-center", targets: 2 },
        ],
        columns : [
            {data : 'id'},
            {data : 'nome'},
            {orderable : false,	data : 'id', "render" : function(id) {
                    return '<a class="btn btn-danger btn-sm btn-block" href="/bolao/excluir/'
                    + id +'" role="button" data-toggle="modal" data-target="#confirm-modal"><i class="fas fa-times-circle"></i></a>';
                }
            }
        ]
    });
});