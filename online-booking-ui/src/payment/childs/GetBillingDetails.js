import React, {Component} from 'react';
import {loadingIndicator} from "../../common/loader/loading-indicator";
import {appNotification} from "../../common/notification/app-notification";
import {API_URL} from "../../common/configuration";
import {handleHttpErrors} from "../../common/HttpHelper";


class GetBillingDetails extends Component {


    constructor(props) {
        super(props);

        this.state = {
            "serviceId": this.props.serviceId,
            "serviceName": this.props.serviceName,
            "slotId": this.props.slotId,
            "firstName": "",
            "lastName": "",
            "email": "",
            "phoneNumber": "",
            "wasValidated": "",

        };

    }


    onReceiveData(paymentResponse) {


        console.log(paymentResponse)
        loadingIndicator.hide();
        this.props.onUpdateBillingDetails(paymentResponse)

    }

    onError(error) {
        loadingIndicator.hide();
        appNotification.showError("Unable to initiate payment  - " + error)
    }

    initiatePayment(evt) {


        const form = evt.currentTarget;



        if (form.checkValidity() === false) {
            return false
        }


        evt.preventDefault();
        evt.stopPropagation();


        loadingIndicator.show();


        const paymentRequest = {
            "selectedSalonServiceDetailId": this.state.serviceId,
            "slotId": this.state.slotId,
            "firstName": this.state.firstName,
            "lastName": this.state.lastName,
            "email": this.state.email,
            "phoneNumber": this.state.phoneNumber
        };


        const requestOptions = {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(paymentRequest)
        };


        const url = API_URL + 'payments/initiate';


        fetch(url, requestOptions)
            .then( res=> handleHttpErrors(res))
            .then(res => res.json())
            .then((results) => this.onReceiveData(results))
            .catch(errorObject=>this.onError(errorObject))

        return false;
    }


    updateState(key, value) {
        this.setState({
            [key]: value,
            "wasValidated": "was-validated"
        })

    }

    render() {

        const { firstName, lastName, email, phoneNumber,wasValidated} = this.state

        return (
            <div className="row">
                <div className="col-12 pl-0">
                    <h3>Enter Billing Details</h3>

                    <form className={wasValidated} onSubmit={(evt) => {return this.initiatePayment(evt)}}>
                        <div className="form-group">
                            <label>First Name</label>
                            <input type="text" className="form-control" required value={firstName}
                                   onChange={(evt) => this.updateState("firstName", evt.target.value)}/>
                        </div>
                        <div className="form-group">
                            <label>Last Name</label>
                            <input type="text" className="form-control" required value={lastName}
                                   onChange={(evt) => this.updateState("lastName", evt.target.value)}/>
                        </div>
                        <div className="form-group">
                            <label>Email address</label>
                            <input type="email" className="form-control" required value={email}
                                   onChange={(evt) => this.updateState("email", evt.target.value)}/>

                        </div>
                        <div className="form-group">
                            <label>Phone Number</label>
                            <input type="tel" className="form-control" required value={phoneNumber}
                                   onChange={(evt) => this.updateState("phoneNumber", evt.target.value)}/>

                        </div>

                        <button type="submit" className="btn btn-primary" >Make Payment</button>
                    </form>

                </div>
            </div>
        );
    }


}


export default GetBillingDetails;
