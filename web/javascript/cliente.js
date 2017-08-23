$(document).ready(function()
{
    $("#guarda").click(function(){
        
        var nombre=$("#nombre").val();
        $.ajax(
        {
            type:'POST',
            url:'cliente',
            data: { action:'guardar', nombre : nombre },
            success: function(data)
            {
               alert(data);
            }
        });
    });
});


