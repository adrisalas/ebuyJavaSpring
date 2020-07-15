function checkSignUp(form) {
        var output = false;

        if(form.nickname.value == "" || form.email1.value == "" || form.email2.value == ""
            || form.password1.value == "" || form.password2.value == ""){
            document.getElementById("signupstatus").innerHTML = "ERROR. Debes rellenar todos los campos para registrarte";
        } else if (form.email1.value != form.email2.value){
            document.getElementById("signupstatus").innerHTML = "ERROR. Los email no coinciden";
        } else if(!validateEmail(form.email1.value)){
            document.getElementById("signupstatus").innerHTML = "ERROR. Email invalido";
        } else if (form.password1.value != form.password2.value){
            document.getElementById("signupstatus").innerHTML = "ERROR. Las contraseñas no coinciden";
        } else {
            output = true;
        }
        return output;
    }
function validateEmail(email) {
    let re = /^[-a-z0-9~!$%^&*_=+}{\'?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i;
    return re.test(email);
}