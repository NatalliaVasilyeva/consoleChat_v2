(function poll() {
    setTimeout(function() {
        $.ajax({ url: "registration", success: function(data) {
            document.getElementById("chat_messages").innerText=data;
             }, dataType: "text", complete: poll });
    }, 30000);
})();