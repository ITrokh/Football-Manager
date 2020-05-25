<#assign
    know= Session.SPRING_SECURITY_CONTEXT?? <#--если обьект определен в контексте спринга, то можем работать с ссесией пользователя-->
>
<#if know>
    <#assign
        user=Session.SPRING_SECURITY_CONTEXT.authentication.principal <#--будет содержать данные о пользователе и позволит использовать его методы-->
        username=user.getUsername()
        currentUserId=user.getId()
        isAdmin=user.isAdmin()
        isUser=user.isUser()
        isTeamLeader=user.isTeamLeader()
        isStudent=user.isStudent()
    >
<#else>
    <#assign
    username="Unauthorized user"
    currenUserId=-1
    isAdmin=false
    isUser=false
    isTeamLeader=false
    isStudent=false
    >

</#if>