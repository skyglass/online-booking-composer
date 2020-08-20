import React, {Component} from 'react';
import {loadingIndicator} from "../../common/loader/loading-indicator";
import {appNotification} from "../../common/notification/app-notification";
import {CardElement, ElementsConsumer} from "@stripe/react-stripe-js";


const CARD_ELEMENT_OPTIONS = {
    style: {
        base: {
            color: "#32325d",
            fontFamily: '"Helvetica Neue", Helvetica, sans-serif',
            fontSmoothing: "antialiased",
            fontSize: "16px",
            "::placeholder": {
                color: "#aab7c4",
            },
        },
        invalid: {
            color: "#fa755a",
            iconColor: "#fa755a",
        },
    },
};

class PayWithStripe extends Component {





    constructor(props) {
        super(props);

        this.state = {
            "initiatePaymentResponse": this.props.initiatePaymentResponse,
            "processingPayment":false
        };

    }




    async handleSubmit  (event,stripe, elements) {

        event.preventDefault();
        event.stopPropagation();

        loadingIndicator.show()
        this.setState({processingPayment:true})
        const {clientSecret,firstName,lastName,email,phoneNumber} = this.state.initiatePaymentResponse

        const name= firstName + " " + lastName


        let data = {
            payment_method: {
                card: elements.getElement(CardElement),
                billing_details: {
                    name: name,
                    email: email,
                    phone: phoneNumber
                },
            }
        };
        const result = await stripe.confirmCardPayment(clientSecret, data);

        if (result.error) {

            console.log(result.error);
            appNotification.showError(result.error.message)
            this.setState({processingPayment:false})
            loadingIndicator.hide()
        } else {
            // The payment has been processed!
            if (result.paymentIntent.status === 'succeeded') {
                console.log(result.paymentIntent);
                this.setState({processingPayment:false})
                loadingIndicator.hide()
                this.props.onPaymentSuccess(result.paymentIntent)
            }
        }
    };


    render() {

        const {processingPayment} = this.state

        return (
            <ElementsConsumer>
                {({stripe, elements}) => (


                    <form  onSubmit={(evt) => this.handleSubmit(evt,stripe,elements)} >

                        <div >
                            <h3>Enter Card details</h3>
                            <CardElement options={CARD_ELEMENT_OPTIONS} />
                        </div>

                        <div className="pt-5"><small>Test Card 4242424242424242</small></div>

                        <button type="submit" className="btn btn-success mt-4" disabled={!stripe || processingPayment}>Pay</button>
                        <br/>

                    </form>



                )}
            </ElementsConsumer>
        );
    }


}


export default PayWithStripe;
