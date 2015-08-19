$(document).ready(function() {
    doAjaxPost();
});

// Sources :
// http://crunchify.com/how-to-use-ajax-jquery-in-spring-web-mvc-jsp-example/
// http://www.javacodegeeks.com/2012/02/ajax-with-spring-mvc-3-using.html
// http://stackoverflow.com/questions/27176640/how-to-put-code-thymeleaf-in-an-external-javascript-file
// http://stackoverflow.com/questions/25692735/spring-security-with-thymeleaf-simple-example
// http://stackoverflow.com/questions/22834428/how-to-set-csrf-tokens-in-requestheader-to-pass-in-post-method-call
// http://stackoverflow.com/questions/3258645/pass-request-headers-in-a-jquery-ajax-get-call
function doAjaxPost() {
        var token = $("meta[name='_csrf']").attr("value");
        var header = $("meta[name='_csrf_header']").attr("value");
        $.ajax({
                type: "POST",
                url: "/showUserDetails",
                headers: {header, token},
                beforeSend: function(xhr){xhr.setRequestHeader( header, token);},
                success: function(data) {
                    //$("#ajax-content").empty();
                    var resultAjax = "";
                    $.each(data, function(i, item) {
                        resultAjax += "<tr><td>"+item.username+"</td><td>"+item.version+"</td><td>"+item.assigned+"</td></tr>";
                        console.log(resultAjax);
                    });
                    $("#ajax-content").append(resultAjax);
                },
                error: function(e){
                        alert('Error: ' + e);
                }
        });
};



