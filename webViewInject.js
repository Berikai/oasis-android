javascript:(function(){
    const login_button = document.getElementsByName("login-button")[0];
    const logout_button = document.getElementsByClassName("bg-danger dropdown-item")[0];

    if (login_button != null) {
        login_button.addEventListener("click", () => {

            const username = document.getElementById("loginform-username");
            const password = document.getElementById("loginform-password");
            const pin = document.getElementById("input-help-hover");
        
            if(username != null) {
                Android.sendCredential("username", username.value);
            }
        
            if(password != null) {
                 Android.sendCredential("password", password.value);
            }
        
            if(pin != null) {
                Android.sendCredential("pin", pin.value);
            }
        });
    }

    if (logout_button != null) {
        logout_button.addEventListener("click", () => {
            Android.logout();
        });
    }
})();