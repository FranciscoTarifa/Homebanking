const { createApp } = Vue

createApp({
    data() {
        return {
            transactions:[],
            cliente:{},
            cuentas:[],
            cuentas2:[],
            transferType:"",
            id:"",
            queryString:"",
            params:"",
            loans:[],
            loanCar:{},
            loanPersonal:{},
            loanMortgage:{},
            loanNameChoose:{},
            loanAmount:"",
            payments:"",
            accountNumberDeposit:{},
            totalWithTax:"",
            nameSlice:"",
            lastNameSlice:"",
            activeAccounts:"",
        }
    },
    created(){
        this.imprimirClientes()
        this.loadLoans()
    },
    mounted(){
    },
    methods:{
        imprimirClientes(){
            axios.get("/api/clients/current")
            .then(respuesta => {
                this.cliente = respuesta.data
                this.cuentas = this.cliente.accounts
                this.nameSlice = this.cliente.firstName.slice(0,1)
                this.lastNameSlice = this.cliente.lastName.slice(0,1)
                this.loans = this.cliente.loans.sort((a,b) => b.id - a.id)
                this.cards = this.cliente.cards.sort((a,b) => b.id - a.id)
                this.activeAccounts = this.cuentas.filter(a => a.accountActive)
            })
        },
        logout(){
            axios.post("/api/logout")
            .then(()=>{
                window.location.href ="/web/index.html"
            })
        },
        loadLoans(){
            axios.get("/loans")
            .then((response)=>{
                this.loans = response.data
                this.loanCar = this.loans.filter(loan => loan.name == "Car")
                this.loanPersonal = this.loans.filter(loan => loan.name == "Personal")
                this.loanMortgage = this.loans.filter(loan => loan.name == "Mortgage")
            })
        },
        loanRequest(){
        Swal.fire({
            title: 'Are you sure?',
            showCancelButton: true,
            confirmButtonText: 'Confirm',
            }).then((result) => {
            if (result.isConfirmed) {
                axios.post("/api/loans",{loanName:this.loanNameChoose,amount:this.loanAmount,payments:this.payments,accountNumberTo:this.accountNumberDeposit})
                .then(() => Swal.fire("The loan was requested successfully","","success"))
                .then((response)=>{
                    window.location.href="/web/accounts.html"
                })
                .catch(error =>
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: `${error.response.data}`,
                    }))
            }
            })
    },
    },
    computed:{
        totalInstallments(){
            if(this.loanNameChoose == 'Car' && this.payments == 6){
                this.totalWithTax = ((this.loanAmount * 1.25) / this.payments)
            }
            if(this.loanNameChoose == 'Car' && this.payments == 12){
                this.totalWithTax = ((this.loanAmount * 1.30) / this.payments)
            }
            if(this.loanNameChoose == 'Car' && this.payments == 24){
                this.totalWithTax = ((this.loanAmount * 1.35) / this.payments)
            }
            if(this.loanNameChoose == 'Car' && this.payments == 36){
                this.totalWithTax = ((this.loanAmount * 1.40) / this.payments)
            }
            if(this.loanNameChoose == 'Personal' && this.payments == 12){
                this.totalWithTax = ((this.loanAmount * 1.25) / this.payments)
            }
            if(this.loanNameChoose == 'Personal' && this.payments == 24){
                this.totalWithTax = ((this.loanAmount * 1.30) / this.payments)
            }
            if(this.loanNameChoose == 'Mortgage' && this.payments == 12){
                this.totalWithTax = ((this.loanAmount * 1.25) / this.payments)
            }
            if(this.loanNameChoose == 'Mortgage' && this.payments == 24){
                this.totalWithTax = ((this.loanAmount * 1.30) / this.payments)
            }
            if(this.loanNameChoose == 'Mortgage' && this.payments == 36){
                this.totalWithTax = ((this.loanAmount * 1.40) / this.payments)
            }
            if(this.loanNameChoose == 'Mortgage' && this.payments == 48){
                this.totalWithTax = ((this.loanAmount * 1.45) / this.payments)
            }
            if(this.loanNameChoose == 'Mortgage' && this.payments == 60){
                this.totalWithTax = ((this.loanAmount * 1.50) / this.payments)
                
            } 
            return Math.round(this.totalWithTax)
        }
    },

}).mount('#app')