const { createApp } = Vue;

createApp({
  data() {
    return {
      email: "",
      password: "",
      newName: "",
      newLastName: "",
      newEmail: "",
      newPassword: "",
    };
  },
  created() {},
  mounted() {},
  methods: {
    login() {
      console.log(this.email);
      console.log(this.password);
      axios
        .post("/api/login", `email=${this.email}&password=${this.password}`, {
          headers: { "content-type": "application/x-www-form-urlencoded" },
        })
        .then(() => {
          window.location.href = "/web/accounts.html";
        })
        .catch(() =>
          Swal.fire({
            icon: "error",
            title: "Oops...",
            text: "The credentials are incorrect.",
          })
        );
    },
    logout() {
      axios.post("/api/logout").then(() => {
        window.location.href = "/web/index.html";
      });
    },
    singUp() {
      axios
        .post(
          "/api/clients",
          `firstName=${this.newName}&lastName=${this.newLastName}&email=${this.newEmail}&password=${this.newPassword}`,
          { headers: { "content-type": "application/x-www-form-urlencoded" } }
        )
        .then((response) =>
          axios.post(
            "/api/login",
            `email=${this.newEmail}&password=${this.newPassword}`,
            { headers: { "content-type": "application/x-www-form-urlencoded" } }
          )
        )
        .then((response) =>
          axios.post(
            "/api/clients/current/accounts",
            `accountType=SAVING`,
            { headers: { "content-type": "application/x-www-form-urlencoded" } }
          )
        )
        .then(() =>
          Swal.fire({
            position: "center",
            icon: "success",
            title: "Your account was created successfully",
            showConfirmButton: false,
            timer: 1500,
          })
        )
        .then(() => (window.location.href = "/web/accounts.html"))
        .catch((error) =>
          Swal.fire({
            icon: "error",
            title: "An error occurred",
            text: `${error.response.data}`,
          })
        );
    },
  },
  computed: {},
}).mount("#app");

const sign_in_btn = document.querySelector("#sign-in-btn");
const sign_up_btn = document.querySelector("#sign-up-btn");
const container = document.querySelector(".container");

sign_up_btn.addEventListener("click", () => {
  container.classList.add("sign-up-mode");
});

sign_in_btn.addEventListener("click", () => {
  container.classList.remove("sign-up-mode");
});
