<#import "parts/common.ftl" as com>
    <#import "parts/login.ftl" as log>
    <@com.page false>
        <#if Session?? && Session.SPRING_SECURITY_LAST_EXCEPTION??>
            <div class="alert alert-danger" role="alert">
                    <#if Session.SPRING_SECURITY_LAST_EXCEPTION.message?contains("Bad credentials")>
                        Sorry, the credentials you're using are invalid(wrong username or password).
                    </#if>
                    <#if Session.SPRING_SECURITY_LAST_EXCEPTION.message?contains("User is disabled")>
                        User is disabled, please check your mail for message with activation link.
                    </#if>
            </div>
        </#if>
        <#if message??>
            <div class="alert alert-${messageType}" role="alert">
                ${message}
            </div>
        </#if>
        <@log.login "/login" false/>

        </@com.page>

