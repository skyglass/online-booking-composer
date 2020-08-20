import React, {Component, Fragment} from 'react';
import {loadingIndicator} from "../../common/loader/loading-indicator";
import {appNotification} from "../../common/notification/app-notification";
import {API_URL} from "../../common/configuration";
import {handleHttpErrors} from "../../common/HttpHelper";

const QRCode = require('qrcode.react');


class ShowConfirmedTicket extends Component {





    constructor(props) {
        super(props);

        this.state = {
            "paymentSuccessResponse": this.props.paymentSuccessResponse,
            "payment": this.props.payment,
            "processing":true,
            "ticketUrl":"",
            "ticketId":"",
            "salonDetails":null,
            "slot":null,


        };

    }


    onReceiveData(ticketResponse){

        console.log("Ticket response",ticketResponse)
        loadingIndicator.hide();

        const ticketUrl =API_URL +"/tickets/" +ticketResponse.ticket.id
        this.setState({
            "processing":false,
            "ticketUrl":ticketUrl,
            salonDetails:ticketResponse.salonDetails,
            slot:ticketResponse.ticket.payment.slot
        })

    }
    onError(error){
        loadingIndicator.hide();
        appNotification.showError("Unable to Generate Ticket  - " + error)
    }



    componentDidMount() {

        this.generateTicket()
    }



    generateTicket(){

        console.log(this.state)

        const {id} = this.state.payment;


        const requestOptions = {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' }
        };


        const url= API_URL + 'payments/confirm/' +id;


        fetch(url,requestOptions)
            .then( res=> handleHttpErrors(res))
            .then(res => res.json())
            .then((results) => this.onReceiveData(results))
            .catch(errorObject=>this.onError(errorObject))



    }


    updateState(key,value){
        this.setState({
            [key]:value
        })
    }

    render() {

        const {processing,ticketUrl} = this.state



        if(processing)
            return (  <button className="btn btn-primary" type="button" disabled>
                <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                <span className="sr-only">Generating Ticket...</span>
            </button>)


        const {name,address,city,state,zipcode,phone} = this.state.salonDetails
        const {stylistName,slotFor} = this.state.slot
        const serviceName = this.state.slot.selectedService.name
        const slotDate = new Date(slotFor).toDateString()

        return (

            <Fragment>
                <h3>Your Ticket Details</h3>
                <div className="row">
                    <div className="col-6">
                        <strong> Service Details</strong>
                        <div>{serviceName} @ {slotDate} By {stylistName} </div>
                        <hr/>
                        <br/>
                        <strong> Salon Address Details</strong>
                        <div>{name}</div>
                        <div>{address}</div>
                        <div>{city}</div>
                        <div>{state}</div>
                        <div>Zip {zipcode}</div>
                        <div>Phone {phone}</div>

                    </div>
                    <div className="col-6">

                        <strong>Take a Picture of the below code and present it to admin </strong>
                        <QRCode value={ticketUrl} />
                    </div>

                </div>

            </Fragment>
        );
    }


}


export default ShowConfirmedTicket;
