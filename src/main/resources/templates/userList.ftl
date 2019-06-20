<#import "parts/common.ftl" as com>

<@com.page false>

<div class="container">

    <div class="row">
        <div class="col-6">
            <div class="row">
                <div class="col-12">
                    <div class="page-header">
                        <h3>Users</h3>
                    </div>
                </div>
            </div>
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Role</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <#list users as user>
                    <tr>
                        <td>${user.username}</td>
                        <td><#list user.roles as role>${role}<#sep>, </#list></td> <#-- <#sep>, директива для разделения каждого элемента-->
                        <td><a href="/user/${user.id}">Edit</a> </td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
        <div class="col-6 text-right">
            <div class="page-header">
                <a href="/registration"><p>Add new user</p></a>
            </div>
        </div>
    </div>
</div>
</@com.page>