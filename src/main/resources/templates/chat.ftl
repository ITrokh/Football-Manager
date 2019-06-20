<#include "parts/security.ftl">
<#import "parts/common.ftl" as com>
<@com.page true>
<noscript>
    <h2>Sorry! Your browser doesn't support Javascript</h2>
</noscript>

    <div class="row">
        <div class="col-6">
            <div id="username-page">
                <div class="username-page-container">
                    <h1 class="title">Global chat</h1>
                    <form id="usernameForm" name="usernameForm">
                        <div class="form-group">
                            <input hidden type="text" id="name" placeholder="Username" value="${user.username}" autocomplete="off" class="form-control" />
                            <input hidden type="text" id="id" placeholder="Username" value="${user.id}" autocomplete="off" class="form-control" />
                        </div>
                        <div class="form-group">
                            <button type="submit" class="accent username-submit">Start Chatting</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <#--какая роль для второго чата-->
        <#if isPlayer || isTeamLeader>
        <div class="col-6">
            <div id="username-page-team">
                <div class="username-page-container_team">
                    <h1 class="title">Team chat</h1>
                    <form id="usernameFormTeam" name="usernameForm">
                        <div class="form-group">
                            <input hidden type="text" id="nameTeam" placeholder="Username" value="${user.username}" autocomplete="off" class="form-control" />
                            <input hidden type="text" id="idTeam" placeholder="Username" value="${user.id}" autocomplete="off" class="form-control" />
                        </div>
                        <div class="form-group">
                            <button type="submit" class="accent username-submit">Start Chatting</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
        </#if>
    <div class="row">
        <div class="col-6">
            <div id="chat-page" class="hidden">
                <div class="chat-container">
                    <div class="chat-header">
                        <h2>Global chat</h2>
                    </div>
                    <div class="connecting">
                        Connecting...
                    </div>
                    <ul id="messageArea">

                    </ul>
                    <form id="messageForm" name="messageForm">
                        <div class="form-group">
                            <div class="input-group clearfix">
                                <input type="text" id="message" placeholder="Type a message..." autocomplete="off" class="form-control"/>
                                <button type="submit" class="primary">Send</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-6">

            <div id="chat-page-team" class="hidden">
                <div class="chat-container">
                    <div class="chat-header">
                        <h2>Team chat</h2>
                    </div>
                    <div class="connecting-team">
                        Connecting...
                    </div>
                    <ul id="messageAreaTeam">

                    </ul>
                    <form id="messageFormTeam" name="messageFormTeam">
                        <div class="form-group">
                            <div class="input-group clearfix">
                                <input type="text" id="messageTeam" placeholder="Type a message..." autocomplete="off" class="form-control"/>
                                <button type="submit" class="primary">Send</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

</@com.page>

<#--<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.4/sockjs.min.js"></script>-->
<#--<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>-->
<#--<script src="main.js"></script>-->
<#--</body>-->
<#--</html>-->