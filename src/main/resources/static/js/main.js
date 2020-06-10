'use strict';

//debugger;
var usernamePage = document.querySelector('#username-page');
var usernamePageTeam = document.querySelector('#username-page-team');

var chatPage = document.querySelector('#chat-page');
var chatPageTeam = document.querySelector('#chat-page-team');

var usernameForm = document.querySelector('#usernameForm');
var usernameFormTeam = document.querySelector('#usernameFormTeam');

var messageForm = document.querySelector('#messageForm');
var messageFormTeam = document.querySelector('#messageFormTeam');

var messageInput = document.querySelector('#message');
var messageInputTeam = document.querySelector('#messageTeam');

var messageArea = document.querySelector('#messageArea');
var messageAreaTeam = document.querySelector('#messageAreaTeam');

var connectingElement = document.querySelector('.connecting');
var connectingElementTeam = document.querySelector('.connecting-team');

var stompClient = null;
var stompClientTeam = null;
var username = null;
var userId = null;
var id = null;
var likeAmount = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    username = document.querySelector('#name').value.trim();
    userId = document.querySelector('#id').value.trim();

    if(username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/wg');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}

function connectTeam(event) {

    username = document.querySelector('#nameTeam').value.trim();
    userId = document.querySelector('#idTeam').value.trim();

    if(username) {
        usernamePageTeam.classList.add('hidden');
        chatPageTeam.classList.remove('hidden');

        var socket = new SockJS('/wt');
        stompClientTeam = Stomp.over(socket);

        stompClientTeam.connect({}, onConnectedTeam, onErrorTeam);
    }
    event.preventDefault();
}

function getMessagesAndFill(chatType) {
    $.ajax({
        url: '/getAllMessages?chatType=' + chatType,
        dataType : "json",
        success: function (data) {
            $.each(data, function(i, val) {
                if (chatType === 'GLOBAL') {
                    onMessageReceived(null, val);
                } else {
                    onMessageReceivedTeam(null, val);
                }
            });
        }
    });
}


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);
    stompClient.subscribe('/topic/like', likeReceived);

    getMessagesAndFill('GLOBAL');

    // Tell your username to the server
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, userId: userId, type: 'JOIN'})
    );

    connectingElement.classList.add('hidden');
}

function onConnectedTeam() {
    // Subscribe to the Public Topic
    stompClientTeam.subscribe('/topic/team', onMessageReceivedTeam);
    stompClientTeam.subscribe('/topic/likeTeam', likeReceivedTeam);

    getMessagesAndFill('TEAM');
    // Tell your username to the server
    stompClientTeam.send("/app/chat.addUserTeam",
        {},
        JSON.stringify({sender: username, userId: userId, type: 'JOIN'})
    );

    connectingElementTeam.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function onErrorTeam(error) {
    connectingElementTeam.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElementTeam.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT',
            userId: userId,
            chatType: 'GLOBAL'
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function sendMessageTeam(event) {
    var messageContent = messageInputTeam.value.trim();
    if(messageContent && stompClientTeam) {
        var chatMessage = {
            sender: username,
            content: messageInputTeam.value,
            type: 'CHAT',
            userId: userId,
            chatType: 'TEAM'
        };
        stompClientTeam.send("/app/chat.sendMessageTeam", {}, JSON.stringify(chatMessage));
        messageInputTeam.value = '';
    }
    event.preventDefault();
}

function likeClick(id) {
    var elementById = document.getElementById(id + 'global');
    var chatMessage = {
        sender: username,
        content: messageInput.value,
        type: 'CHAT',
        userId: userId,
        chatType: 'GLOBAL',
        id: id
    };
    stompClient.send("/app/chat.like", {}, JSON.stringify(chatMessage));
}


function likeClickTeam(id) {
    var elementById = document.getElementById(id + 'team');

    var chatMessage = {
        sender: username,
        content: messageInput.value,
        type: 'CHAT',
        userId: userId,
        chatType: 'TEAM',
        id: id
    };
    stompClientTeam.send("/app/chat.likeTeam", {}, JSON.stringify(chatMessage));
}

// Use this method in loop to build list of messages
function onMessageReceived(payload, messageDTO) {
    var message = null;
    if (messageDTO != null) {
        message = messageDTO;
    } else {
        message = JSON.parse(payload.body);
    }
    id = message.id;
    likeAmount = message.likeAmount;

    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var userAHref = document.createElement('a');
        userAHref.href = '/user-messages/' + userId;
        userAHref.target = '_blank';
        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        userAHref.appendChild(usernameElement);

        messageElement.appendChild(userAHref);

        var likes = document.createElement('a');
        likes.classList.add('col');
        likes.classList.add('text-right');
        likes.id = id + 'globalA';
        likes.href = 'javascript:likeClick(\'' + id + '\')';

        var iconLike = document.createElement('i');
        iconLike.classList.add('far');
        iconLike.classList.add('fa-heart');
        iconLike.classList.add('like-icon');
        iconLike.id = id + 'global';
        iconLike.style['color'] = 'blue';

        likes.href = 'javascript:likeClick(\'' + id + '\')';
        likes.text = likeAmount;
        likes.appendChild(iconLike);
        messageElement.appendChild(likes);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function likeReceived(payload) {
    var message = JSON.parse(payload.body);
    var elementById = document.getElementById(message.id + 'globalA');
    elementById.text = message.likeAmount;

    var iconLike = document.createElement('i');
    iconLike.classList.add('far');
    iconLike.classList.add('fa-heart');
    iconLike.classList.add('like-icon');
    iconLike.style['color'] = 'blue';
    iconLike.id = id + 'global';
    elementById.appendChild(iconLike);
}

function onMessageReceivedTeam(payload, messageDTO) {
    var message = null;
    if (messageDTO != null) {
        message = messageDTO;
    } else {
        message = JSON.parse(payload.body);
    }
    id = message.id;
    likeAmount = message.likeAmount;

    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var userAHref = document.createElement('a');
        userAHref.href = '/user-messages/' + userId;
        userAHref.target = '_blank';
        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        userAHref.appendChild(usernameElement);

        messageElement.appendChild(userAHref);

        var likes = document.createElement('a');
        likes.classList.add('col');
        likes.classList.add('text-right');
        likes.id = id + 'teamA';
        likes.href = 'javascript:likeClickTeam(\'' + id + '\')';

        var iconLike = document.createElement('i');
        iconLike.classList.add('far');
        iconLike.classList.add('fa-heart');
        iconLike.classList.add('like-icon');
        iconLike.style['color'] = 'blue';
        iconLike.id = id + 'team';
        likes.text = likeAmount;
        likes.appendChild(iconLike);

        messageElement.appendChild(likes);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageAreaTeam.appendChild(messageElement);
    messageAreaTeam.scrollTop = messageAreaTeam.scrollHeight;
}

function likeReceivedTeam(payload) {
    var message = JSON.parse(payload.body);
    var elementById = document.getElementById(message.id + 'teamA');
    elementById.text = message.likeAmount;

    var iconLike = document.createElement('i');
    iconLike.classList.add('far');
    iconLike.classList.add('fa-heart');
    iconLike.classList.add('like-icon');
    iconLike.style['color'] = 'blue';
    iconLike.id = id + 'team';
    elementById.appendChild(iconLike);
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener('submit', connect, true);
usernameFormTeam.addEventListener('submit', connectTeam, true);
messageForm.addEventListener('submit', sendMessage, true);
messageFormTeam.addEventListener('submit', sendMessageTeam, true);