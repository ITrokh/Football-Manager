<#import "parts/common.ftl" as com>
<@com.page false>
<b>Edit '${user.fullName}'</b>

    <form action="/user" method="post">
        <div class="form-group row">
            <label class="col-sm-1 col-form-label">UserName:</label>
            <div class="col-sm-6">
                <input type="text" name="username" value="${user.username}" placeholder="UserName"/>
            </div>
        </div>
        <#list roles as role>
            <div>
                <label><input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}>${role}</label>
            </div>
        </#list>
        <#if user.getStudent()??>
            <div class="form-group row">
                <label class="col-sm-1 col-form-label">Group:</label>
                <div class="col-sm-6">
                    <input type="text" name="group" value="${user.getStudent().group}" placeholder="Group"/>
                </div>
            </div>
        </#if>
        <input type="hidden" value="${user.id}" name="userId">
        <input type="hidden" value="${_csrf.token}" name="_csrf">
        <button type="submit">Save</button>
    </form>
</@com.page>
<#--user.roles?seq_contains проверка наличия элемента в колекции и переобразовываем в стринг(выбран и ничего)-->