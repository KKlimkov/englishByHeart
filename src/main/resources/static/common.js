function showAlert(messageContainerId, messageText, alertType = 'success') {
    const messageContainer = document.getElementById(messageContainerId);
    const message = document.createElement('div');
    message.classList.add('alert', `alert-${alertType}`, 'fade-out');
    message.textContent = messageText;
    messageContainer.innerHTML = '';
    messageContainer.appendChild(message);
    setTimeout(() => {
        message.classList.add('hidden');
        setTimeout(() => {
            message.remove();
        }, 1000);
    }, 3000); // Remove after 5 seconds
}