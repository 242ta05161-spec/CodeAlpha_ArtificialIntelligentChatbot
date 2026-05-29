async function sendMessage() {

    const input = document.getElementById("user-input");

    const message = input.value.trim();

    if(message === "") return;

    const chatBox = document.getElementById("chat-box");

    // User Message
    const userDiv = document.createElement("div");

    userDiv.className = "user-message";

    userDiv.innerHTML = message;

    chatBox.appendChild(userDiv);

    chatBox.scrollTop = chatBox.scrollHeight;

    input.value = "";

    // Loading Message
    const loadingDiv = document.createElement("div");

    loadingDiv.className = "bot-message";

    loadingDiv.innerHTML = "🤖 Typing...";

    chatBox.appendChild(loadingDiv);

    chatBox.scrollTop = chatBox.scrollHeight;

    // Fetch response
    const response = await fetch("/chat", {
        method: "POST",
        body: message
    });

    const botReply = await response.text();

    // Remove loading
    chatBox.removeChild(loadingDiv);

    // Bot Message
    const botDiv = document.createElement("div");

    botDiv.className = "bot-message";

    botDiv.innerHTML = "🤖 " + botReply;

    chatBox.appendChild(botDiv);

    chatBox.scrollTop = chatBox.scrollHeight;
}

// Enter Key Support
function handleKey(event){

    if(event.key === "Enter"){
        sendMessage();
    }

}