<#-- ws-test.ftl -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>WebSocket Test</title>
    <style>
        /* --- Body & Layout --- */
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
            color: #fff;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: flex-start;
            min-height: 100vh;
            padding: 2rem;
            overflow-x: hidden;
            animation: fadeInBody 1s ease-out;
        }

        @keyframes fadeInBody {
            0% {opacity: 0; transform: translateY(-20px);}
            100% {opacity: 1; transform: translateY(0);}
        }

        /* --- Header --- */
        h1 {
            font-size: 2.5rem;
            margin-bottom: 1rem;
            text-shadow: 2px 2px 8px rgba(0,0,0,0.3);
            animation: slideInHeader 1s ease-out;
        }

        @keyframes slideInHeader {
            0% {opacity:0; transform: translateX(-100px);}
            100% {opacity:1; transform: translateX(0);}
        }

        /* --- Log Box --- */
        #log {
            width: 90%;
            max-width: 600px;
            height: 300px;
            border-radius: 12px;
            padding: 1rem;
            overflow-y: auto;
            background-color: rgba(255,255,255,0.1);
            margin-bottom: 1rem;
            box-shadow: 0 4px 15px rgba(0,0,0,0.3);
            font-family: monospace;
            backdrop-filter: blur(8px);
            transition: transform 0.3s;
        }

        /* --- Inputs & Buttons --- */
        input, button {
            padding: 0.5rem 1rem;
            margin: 0.25rem;
            border-radius: 8px;
            border: none;
            font-size: 1rem;
            outline: none;
            transition: transform 0.2s, box-shadow 0.2s, background 0.2s;
        }

        input {
            width: 250px;
            max-width: 80vw;
        }

        button {
            cursor: pointer;
            color: #fff;
            background: linear-gradient(45deg, #ff7eb3, #ff758c);
            box-shadow: 0 4px 10px rgba(0,0,0,0.3);
        }

        button:not(:disabled):hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 15px rgba(0,0,0,0.4);
        }

        button:disabled {
            cursor: not-allowed;
            background: linear-gradient(45deg, #d8a0b8, #d88f99);
            opacity: 0.7;
            box-shadow: none;
            transform: none;
        }

        .controls {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            margin-bottom: 1rem;
            gap: 0.5rem;
        }

        /* --- Messages Animation --- */
        p {
            margin: 0.25rem 0;
            animation: fadeInMsg 0.3s ease-out;
        }

        @keyframes fadeInMsg {
            0% {opacity: 0; transform: translateX(-10px);}
            100% {opacity: 1; transform: translateX(0);}
        }
    </style>
</head>
<body>

<h1>
    <img src="/assets/images/logo-icon.svg" alt="Ktor Logo"
         style="height: 1em; vertical-align: middle; margin-right: 0.5em;">
</h1>
<h1>WebSocket</h1>

<div class="controls">
    <input type="text" id="wsUrl" value="${wsUrl}">
    <button id="connectBtn">Connect</button>
    <button id="disconnectBtn" disabled>Disconnect</button>
</div>

<div id="log"></div>

<div class="controls">
    <input type="text" id="messageInput" placeholder="Type a message..." disabled>
    <button id="sendBtn" disabled>Send</button>
    <button id="cleanBtn" disabled>Clean</button>
</div>

<script>
    <#noparse>
    let socket = null;
    const wsUrlInput = document.getElementById("wsUrl");
    const connectBtn = document.getElementById("connectBtn");
    const disconnectBtn = document.getElementById("disconnectBtn");
    const logDiv = document.getElementById("log");
    const messageInput = document.getElementById("messageInput")
    const sendBtn = document.getElementById("sendBtn");
    const cleanBtn = document.getElementById("cleanBtn");

    function log(message, type = "info") {
        const p = document.createElement("p");
        p.textContent = message;
        if (type === "error") p.style.color = "#ff6b6b";
        if (type === "sent") p.style.color = "#48dbfb";
        logDiv.appendChild(p);
        logDiv.scrollTop = logDiv.scrollHeight;
    }

    connectBtn.onclick = () => {
        const url = wsUrlInput.value;
        if (!url) return alert("Enter WebSocket URL");

        // Disable connect, enable disconnect
        connectBtn.disabled = true;
        wsUrlInput.disabled = true;
        messageInput.disabled = false;
        disconnectBtn.disabled = false;
        sendBtn.disabled = false;
        cleanBtn.disabled = false;

        socket = new WebSocket(url);

        socket.onopen = () => log(`Connected to ${url}`, "info");
        socket.onmessage = (event) => log(`Received: ${event.data}`, "info");
        socket.onclose = () => {
            log(`Disconnected`, "error");
            logDiv.innerHTML = "";
            connectBtn.disabled = false;
            wsUrlInput.disabled = false;
            messageInput.disabled = true;
            disconnectBtn.disabled = true;
            sendBtn.disabled = true;
            cleanBtn.disabled = true;
        };
        socket.onerror = (err) => log(`Error: ${err.message || err}`, "error");
    };

    disconnectBtn.onclick = () => {
        if (socket) {
            socket.close();
            socket = null;
        }
    };

    document.getElementById("sendBtn").onclick = () => {
        const message = messageInput.value;
        if (socket && socket.readyState === WebSocket.OPEN) {
            socket.send(message);
            log(`Sent: ${message}`, "sent");
        } else {
            alert("WebSocket is not connected");
        }
    };

    document.getElementById("cleanBtn").onclick = () => {
        logDiv.innerHTML = "";
    };
    </#noparse>
</script>

</body>
</html>
