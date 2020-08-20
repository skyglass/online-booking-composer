import React, {Component} from 'react';
import {withRouter} from "react-router-dom";
import {loadingIndicator} from "../common/loader/loading-indicator";
import {appNotification} from "../common/notification/app-notification";
import {API_URL} from "../common/configuration";
import {getFormattedTime, getTomorrow, isDateGreaterThanToday, isDateLesserThanToday} from "../common/DateHelper";
import {handleHttpErrors} from "../common/HttpHelper";


class ChooseSlot extends Component {



    constructor(props) {
        super(props);

        this.state = {
            "serviceId": this.props.match.params.serviceId,
            "serviceName": this.props.match.params.serviceName,
            "slotDate": getTomorrow(),
            slots:[]
        };

    }

    componentDidMount() {

    }


    setDate(val){
        this.setState({
            "slotDate":val
        })
    }



    filterAndFormat(items){

        return items.filter(item=> isDateGreaterThanToday(item.slotFor) )
            .map(item=>{

                item.time= getFormattedTime(item.slotFor)
                return item
            })
    }

     onReceiveData(items){


        loadingIndicator.hide();

        const slots = this.filterAndFormat(items)

        if(slots.length === 0){
            appNotification.showError("No Slots available" );
            return;
        }

        this.setState({
            slots: slots
        });



    }

    onError(error){
        loadingIndicator.hide();
        appNotification.showError("Unable to retrieve Slots  - " + error)
    }

    showSlotsOnDate(){
        const {serviceId,slotDate} = this.state

        if(isDateLesserThanToday(slotDate)){
            appNotification.showError("Selected date cannot be booked" );
            return
        }

        this.setState({
            slots: []
        });

        loadingIndicator.show();




        const url= API_URL + 'slots/retrieveAvailableSlots/'+serviceId +'/'+ slotDate;

        fetch(url)
            .then( res=> handleHttpErrors(res))
            .then(res => res.json())
            .then((results) => this.onReceiveData(results))
            .catch(errorObject=>this.onError(errorObject))



    }


    render() {

        const {slotDate,serviceName,slots} = this.state


        return (

            <div>
                <div className="row">
                    <div className="col-4"><strong>Choose a Date for {serviceName}</strong></div>
                    <div className="col-5"> <input  className="form-control form-control-lg"
                                                    type="date"
                                                    value={slotDate}
                                                    onChange={(evt)=>this.setDate(evt.target.value)} /> </div>
                    <div className="col-3"> <button type="submit" className="btn btn-primary mb-2" onClick={(evt) => this.showSlotsOnDate()} >Show Slots</button></div>
                </div>

                {slots.length > 0 && <h4 className="pt-5">Available Slots on {slotDate} <br/> </h4>}

                <div className="grid-container row  text-center">



                    {slots.map((item, index)=>{
                        return(
                            <div key={index} className="card mb-4 shadow-sm">
                                <div className="card-header">
                                    <h4 className="my-0 font-weight-normal">{serviceName}</h4>
                                </div>
                                <div className="card-body">
                                    <h1 className="card-title pricing-card-title">{item.stylistName} </h1>
                                    <ul className="list-unstyled mt-3 mb-4">
                                        <li>Slot Time {item.time}</li>
                                    </ul>
                                    <button type="button" onClick={(evt) => this.bookSlotFor(item)}  className="btn btn-lg btn-block btn-outline-primary">Book this Slot</button>
                                </div>
                            </div>
                        );


                    })}

                </div>



            </div> );
    }

    bookSlotFor(item) {

        const {serviceId,serviceName} = this.state

        this.props.history.push("/makepayment/"+item.id+"/"+ serviceId+"/"+ serviceName)
    }
}



export default withRouter(ChooseSlot);
