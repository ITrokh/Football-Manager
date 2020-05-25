<#include "parts/security.ftl">
<#import "parts/common.ftl" as com>
<@com.page false>

    <div class="container">
        <div class="row">
            <div class="col-8">
                <div class="row">
                    <div class="col-12">
                        <div class="page-header">
                            <h3>Your groups</h3>
                        </div>
                    </div>
                </div>
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>Created date</th>
                        <th>Name</th>
                        <th>Students in Group</th>
                        <th>Trainer</th>

                    </tr>
                    </thead>
                    <tbody>
                    <#list teams as team>
                        <tr>
                            <td>${team.createdDate}</td>
                            <td>${team.name}</td>
                            <td>${team.playerAmount}</td>
                            <td>${team.leader}</td>
                        <#if isTeamLeader>
                            <td><a href="/teams/${team.id}/players">Manage group</a></td>
                            <#else><td><a href="/teams/${team.id}/players">See group participants</a></td>
                        </#if>
                                </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
    <#if isTeamLeader>
            <div class="col-4">
                <form action="/teams/add" method="post">
                    <#--<div class="form-group row">-->
                    <label class="col-form-label">Team name:
                        <input type="text" name="name" class="form-control">
                    </label>
                    <input type="hidden" value="${_csrf.token}" name="_csrf">
                    <#--</div>-->
                    <button class="btn btn-primary" type="submit">Create team</button>
                </form>
            </div>
        </#if>
        </div>
    </div>
</@com.page>