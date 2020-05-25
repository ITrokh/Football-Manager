<#import "parts/common.ftl" as com>
<#include "parts/security.ftl">
<@com.page false>

    <div class="container">
        <div class="row">

            <div class="col-6">
                <div class="row">
                    <div class="col-12">
                        <div class="page-header">
                            <h3>Players in team</h3>
                        </div>
                    </div>
                </div>
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Position</th>
    <#if isTeamLeader>
                        <th>Edit</th>
        </#if>
                    </tr>
                    </thead>
                    <tbody>
                    <#list players as player>
                        <tr>
                            <td><a href="/teams/player/${player.id}">${player.name}</a></td>
                            <td>${player.group}</td>
                        <#if isTeamLeader>
                            <td>
                                <a href="/teams/${teamId}/remove_player/${player.id}">
                                    Remove
                                </a>
                            </td>
                            </#if>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
    <#if isTeamLeader>
            <div class="col-6">
                <div class="row">
                    <div class="col-12">
                        <div class="page-header">
                            <h3>Posible players</h3>
                        </div>
                    </div>
                </div>
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Group</th>

                        <th>Edit</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list possiblePlayers as player>
                        <tr>
                            <td>${player.name}</td>
                            <td>${player.group}</td>

                            <td>
                                <a href="/teams/${teamId}/add_player/${player.id}">
                                    Add to team
                                </a>
                            </td>

                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
    </#if>
        </div>
    </div>
</@com.page>