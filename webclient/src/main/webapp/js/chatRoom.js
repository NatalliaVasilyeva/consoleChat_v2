 // (function poll() {
 //    setTimeout(function() {
 //        $.ajax({ url: "chat", success: function(data) {
 //           document.getElementById("messageArea").innerText=data;
 //              }, dataType: "text", complete: poll });
 //     }, 30000);
 // })();


let messageInput = document.getElementById('#message');
console.log(messageInput);


$(document).ready(function () {
    function getData() {
        $.ajax({
            url: "chat",
            type: "GET",
            dataType: "text/xml",
               context: document.body,
            success: function (data) {
                setMessage(data);
                getData();
            },
            error: function () {
                setTimeout(getData, 10000);
            }
        });
    }


    getData();
});

 $(document).ready(function(){
     $("sendMsg").click(function(){
         $.post("/chat",
             {
                 message: messageInput,

             });
     });
 });


function setMessage(data) {
    let currentHTML = document.getElementById('#messageArea').innerHTML;
    let newElem;
    newElem = '<p><span>' + data + '</span></p>';
    document.getElementById('#messageArea').innerHTML = currentHTML + newElem;

}

