import React, {Component} from 'react';
import {withRouter} from "react-router-dom";
import GetBillingDetails from "./childs/GetBillingDetails";
import PayWithStripe from "./childs/PayWithStripe";
import ShowConfirmedTicket from "./childs/ShowConfirmedTicket";

class MakePaymentContainer extends Component {


    constructor(props) {
        super(props);

        this.state = {
            "serviceId": this.props.match.params.serviceId,
            "serviceName": this.props.match.params.serviceName,
            "slotId": this.props.match.params.slotId,
            "initiatePaymentResponse": null,
            "paymentSuccessResponse":null

        }
    }
    updatePaymentResponse(res){
        console.log("updating payment response",res)
        this.setState({
            "initiatePaymentResponse": res
        })
    }

    onPaymentSuccess(paymentSuccessResponse){

        //Now
        this.setState({
            "paymentSuccessResponse": paymentSuccessResponse
        })


    }
    render() {

        const {serviceId, serviceName, slotId, initiatePaymentResponse,paymentSuccessResponse} = this.state

        let container

        if (null == initiatePaymentResponse)
            container =
                <GetBillingDetails serviceId={serviceId} serviceName={serviceName} slotId={slotId}  onUpdateBillingDetails={(evt) => this.updatePaymentResponse(evt)} ></GetBillingDetails>
        else if(null == paymentSuccessResponse)
            container =  <PayWithStripe initiatePaymentResponse={initiatePaymentResponse}  onPaymentSuccess={(evt) => this.onPaymentSuccess(evt)} ></PayWithStripe>
        else
            container = <ShowConfirmedTicket paymentSuccessResponse={paymentSuccessResponse} payment={initiatePaymentResponse}></ShowConfirmedTicket>

        return (
            <div>
                {container}
            </div>
        );
    }


}


export default withRouter(MakePaymentContainer);
