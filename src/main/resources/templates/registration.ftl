<#import "parts/common.ftl" as com>
<#import "parts/login.ftl" as log>
<@com.page false>
<div class="mb-1">Registrate new User</div>
${message?if_exists}
    <@log.login "/registration" true/>
</@com.page>
