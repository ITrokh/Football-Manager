<#import "parts/common.ftl" as com>
<#include "parts/security.ftl">
<@com.page false>
    <#if user??>
        <h5>Hello, ${user.getFullName()}!</h5>
    <#else><h5>Hello, user!</h5>
    </#if>
    <div>Welcome to my diploma app</div>
</@com.page>