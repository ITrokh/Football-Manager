<#include "security.ftl">
<#import "login.ftl" as log>
<nav class="navbar navbar-expand-lg navbar-light bg-light"><#--цветовая схема и подстройка под размер-->
    <a class="navbar-brand" href="/">Football</a><#--название проекта-->
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
<#--меню, которое будет появлятся на экранах меньше, чем large(оно будет схлоповаться)-->
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/">Home</a>
            </li>
            <#if user??>
                <li class="nav-item">
                    <a class="nav-link" href="/chat">Chat</a>
                </li>
            <#if isAdmin>
                <li class="nav-item">
                    <a class="nav-link" href="/main"> All Messages</a>
                </li>
                </#if>
                <li class="nav-item">
                    <a class="nav-link" href="/user-messages/${currentUserId}">My messages</a>
                </li>
            </#if>
            <#if isAdmin>
                <li class="nav-item">
                    <a class="nav-link" href="/user">User list</a>
                </li>
            </#if>
            <#if user??>
                <li class="nav-item">
                    <a class="nav-link" href="/user/profile">Profile</a>
                </li>
            </#if>
            <#if isTeamLeader|| isPlayer>
                <li class="nav-item">
                    <a class="nav-link" href="/team">Teams</a>
                </li>
            </#if>
        </ul>
        <#if user??>
        <div class="text-info mr-3">Your username: ${username}</div>
        </#if>
        <@log.logout />
    </div>
</nav>