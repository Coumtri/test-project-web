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
                success: function(html) {
                alert("succes pour julien!");
                    $("#ajax-content").empty().append(html);
                },
                error: function(e){
                        alert('Error: ' + e);
                        }
        });
        };
/*
var addHeader = function () {
    var token = $("meta[name='_csrf']").attr("value");
    var header = $("meta[name='_csrf_header']").attr("value");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
};*/

// Source : http://stackoverflow.com/questions/907634/is-this-how-you-define-a-function-in-jquery
var extractTable = function(responseParam)
{
        var response = $.parseJSON($responseParam);
        $.each($response, function(i, item) {
            var $tr = $('<tr>').append(
                $('<td>').text(item.rank),
                $('<td>').text(item.content),
                $('<td>').text(item.UID)
            ); //.appendTo('#records_table');
            console.log($tr.wrap('<p>').html());
        });
};
