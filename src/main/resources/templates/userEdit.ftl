<#import "parts/common.ftl" as com>

<@com.page false>
    User Editor

    <form action="/user" method="post">
        <input type="text" name="username" value="${user.username}">
        <#list roles as role>
            <div>
                <label><input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}>${role}</label>
            </div>
        </#list>
        <input type="hidden" value="${user.id}" name="userId">
        <input type="hidden" value="${_csrf.token}" name="_csrf">
        <button type="submit">Save</button>
    </form>
</@com.page>
<#--user.roles?seq_contains проверка наличия элемента в колекции и переобразовываем в стринг(выбран и ничего)-->