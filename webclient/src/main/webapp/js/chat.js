let userName = null;
let websocket = null;

function init() {
    if (!("WebSocket" in window)) {
        alert("Websocket not supported");
    } else {
        while (userName === null) {
            userName = prompt("Enter user name");
            if (userName === "") {
                userName = null;
            }
        }
        websocket = new WebSocket('ws://localhost:8080/webchatik/chat');
        websocket.onopen = function () {
            document.getElementById("reg").style.display = "block";
            console.log("open");
        };
        websocket.onmessage = function (message) {
            setMessage(message.data);
        };
        websocket.onerror = function (data) {
            alert('An error occured, closing application');
            document.getElementById("error").style.display = "block";
            setErrorMessage(data);
            cleanUp();

        };

        websocket.onclose = function (data) {
            if (data.wasClean) {
                console.log("close");
            }
            cleanUp();
        };
    }
}

function cleanUp() {
    document.getElementById("main").style.display = "none";
    userName = null;
    websocket = null;
}

function closeSession() {
    document.getElementById("welcome").style.display = "none";
    document.getElementById("bye").style.display = "block";
    document.getElementById("main").style.display = "none";
    console.log("close method");
    let message = "/exit";
    setMessage(message);
    websocket.send(message);
    cleanUp();
}

function leave() {
    console.log("leave method");
    let message = "/leave";
    setMessage(message);
    websocket.send(message);
}

function sendRegister() {
    console.log("register");
    let message;
    if (document.getElementById("client").checked) {
        message = '/reg client ' + userName;
    }
    if (document.getElementById("agent").checked) {
        message = '/reg agent ' + userName;
    }

    console.log(message);
    websocket.send(message);
    document.getElementById("reg").style.display = "none";
    document.getElementById("main").style.display = "block";
}

function sendMessage() {
    let message = document.getElementById("message").value;
    setMessage(message);
    document.getElementById("message").value = '';
    websocket.send(message);
}

function setMessage(msg) {
    let currentHTML = document.getElementById('scrolling-messages').innerHTML;
    let newElem;
    newElem = '<p><span>' + msg + '</span></p>';
    document.getElementById('scrolling-messages').innerHTML = currentHTML
        + newElem;
}

function setErrorMessage(msg) {
    let currentHTML = document.getElementById('messages').innerHTML;
    let newElem;
    newElem = '<p><span>' + msg + '</span></p>';
    document.getElementById('messages').innerHTML = currentHTML
        + newElem;
}