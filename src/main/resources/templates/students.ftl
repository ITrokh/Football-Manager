<#import "parts/common.ftl" as com>
<#include "parts/security.ftl">
<@com.page false>

    <div class="container">
        <div class="row">

            <div class="col-6">
                <div class="row">
                    <div class="col-12">
                        <div class="page-header">
                            <h3>Students in group</h3>
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
                    <#list students as student>
                        <tr>
                            <td><a href="/teams/student/${student.id}">${student.name}</a></td>
                            <td>${student.group}</td>
                        <#if isTeamLeader>
                            <td>
                                <a href="/teams/${teamId}/remove_student/${student.id}">
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
                            <h3>Students without group</h3>
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
                    <#list possibleStudents as student>
                        <tr>
                            <td>${student.name}</td>
                            <td>${student.group}</td>

                            <td>
                                <a href="/teams/${teamId}/add_student/${student.id}">
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