function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#imgEscudo').attr('src', e.target.result);
        };

        reader.readAsDataURL(input.files[0]);
    }
}

function format ( data ) {
    var s = '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">';
    $.each( data.times, function( key, time ) {
        s += '<tr><td><img width="32" height="32" src="data:image/png;base64,'+time.escudo.data+'" /></td>';
        s += '<td style="width: 500px">'+time.nome+'</td></tr>';
    });
    s += '</table>';
    return s;
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
        	{ width: "5%", className: "text-center", targets: 1 },
        	{ width: "8%", className: "text-center", targets: 2 },
        	{ width: "35%", targets: 3 },
        	{ width: "10%", targets: 4 },
        	{ width: "15%", targets: 5 },
        	{ width: "4%", className: "text-center", targets: 6 },
        	{ width: "4%", className: "text-center", targets: 7 },
        ],
        columns : [
        	{
        		"className": 'details-control',
                "orderable": false,
                "data": null,
                "defaultContent": '',
                "render": function () {
                    return '<i class="fa fa-plus-square" aria-hidden="true"></i>';
                },
                width:"4%"
            },
            {data : 'id'},
            {orderable : false, data: 'escudo.data', "render" : function(img) {
            		return '<img src="data:image/png;base64,'+img+'" width="32" height="32" />';	
            	}	
            },
            {data : 'nome'},
            {data : 'rodadas'},
            {data : 'qtdeTimes'},
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
            
    $('#table-campeonatos tbody').on('click', 'td.details-control', function () {
        var tr = $(this).closest('tr');
        var tdi = tr.find("i.fa");
        var row = table.row(tr);

        if (row.child.isShown()) {
            // This row is already open - close it
            row.child.hide();
            tr.removeClass('shown');
            tdi.first().removeClass('fa-minus-square');
            tdi.first().addClass('fa-plus-square');
        }
        else {
            // Open this row
            row.child(format(row.data())).show();
            tr.addClass('shown');
            tdi.first().removeClass('fa-plus-square');
            tdi.first().addClass('fa-minus-square');
        }
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
	
	$(function() {
		var pais = $('#pais').val();
		if (pais != '') {
			loadTimes(pais);
		}
	});
	
	
	$('#pais').change(function(e) {
		var pais = $(this).val();
		loadTimes(pais);
	});
		
	function loadTimes(pais) { 
		$.getJSON( "/paises.json", function( paises ) {
			$.each( paises, function( key, val ) {
				if (pais == val.name) {
					$.ajax({
						method: "GET",
						url: "/times/buscarPeloPais?pais="+pais,
						beforeSend: function() {
							var qtdeItens = $("#listSelecionaveis a").length;
							if (qtdeItens > 1) {
								$("#listSelecionaveis a").each(function(index) {
									if (index > 0) {
										$(this).remove();
									}
								});
							}							
						},
						success : function(times) {
							$.each(times, function(i, v) {
								var selecionados = $("#listSelecionados input:checked:not('.all')");
								var item = '<a href="#" class="list-group-item list-group-item-action"><img width="16" height="16" class="float-left" src="data:image/png;base64,'+v.escudo.data+'" />'+v.nome+'<input type="checkbox" name="times" class="float-right" value="'+v.id+'"></a>';
								var achou = false;
								$.each(selecionados, function(j, selecionado) {
									console.log($(this).val(), v.id);
									if ($(this).val() == v.id) {
										achou = true;
									}
								});
								if (!achou) {
									$("#listSelecionaveis").append(item);
								}
							});
						},
						statusCode: {
							404: function() {
								bootbox.alert('Não há times cadastrados para este país');
							}
						}
					});
				}
			});
		});
	}
	
	
	/******************************************* PICK LIST DE TIMES ************************************/
	
	$('.add').click(function() {
		var $qtdeTimes = $("#qtdeTimes").val();
		if ($qtdeTimes == '0') {
			bootbox.alert('Primeiro, digite quantos times participarão.');
			 $("#qtdeTimes").focus();
			return;
		}
		
	    $('.all').prop("checked",false);
	    var items = $("#listSelecionaveis input:checked:not('.all')");
	    var n = items.length;
	    if ($qtdeTimes != n) {
	    	bootbox.alert('Selecione os '+$qtdeTimes+' times participantes.');
			return;
	    } else {
	    	items.each(function(idx,item) {
	    		var choice = $(item);
	    		choice.prop("checked",false);
	    		choice.parent().appendTo("#listSelecionados");
	        
	    	});
	    }
	});

	$('.remove').click(function(){
	    $('.all').prop("checked",false);
	    var items = $("#listSelecionados input:checked:not('.all')");
		items.each(function(idx,item){
	      var choice = $(item);
	      choice.prop("checked",false);
	      choice.parent().appendTo("#listSelecionaveis");
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