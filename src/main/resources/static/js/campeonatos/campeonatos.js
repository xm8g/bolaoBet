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
	
	
	$("#btnSearchPaises").click(function() {
		if ($("#pais").val() != '') {
			DWRConsultas.buscarTimesPeloPais('aaa');
		}
	});
	
	
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
    
	/************************ AUTOCOMPLETE DE PAÍSES **************************************/
	
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
	
	
	/******************************************* PICK LIST DE TIMES ************************************/
	
	$('.add').click(function(){
	    $('.all').prop("checked",false);
	    var items = $("#list1 input:checked:not('.all')");
	    var n = items.length;
	  	if (n > 0) {
	      items.each(function(idx,item){
	        var choice = $(item);
	        choice.prop("checked",false);
	        choice.parent().appendTo("#list2");
	      });
	  	}
	    else {
	  		alert("Choose an item from list 1");
	    }
	});

	$('.remove').click(function(){
	    $('.all').prop("checked",false);
	    var items = $("#list2 input:checked:not('.all')");
		items.each(function(idx,item){
	      var choice = $(item);
	      choice.prop("checked",false);
	      choice.parent().appendTo("#list1");
	    });
	});

	/* toggle all checkboxes in group */
	$('.all').click(function(e){
		e.stopPropagation();
		var $this = $(this);
	    if($this.is(":checked")) {
	    	$this.parents('.list-group').find("[type=checkbox]").prop("checked",true);
	    }
	    else {
	    	$this.parents('.list-group').find("[type=checkbox]").prop("checked",false);
	        $this.prop("checked",false);
	    }
	});

	$('[type=checkbox]').click(function(e){
	  e.stopPropagation();
	});

	/* toggle checkbox when list group item is clicked */
	$('.list-group a').click(function(e){
	  
	    e.stopPropagation();
	  
	  	var $this = $(this).find("[type=checkbox]");
	    if($this.is(":checked")) {
	    	$this.prop("checked",false);
	    }
	    else {
	    	$this.prop("checked",true);
	    }
	  
	    if ($this.hasClass("all")) {
	    	$this.trigger('click');
	    }
	});

	
	
});