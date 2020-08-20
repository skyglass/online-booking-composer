import React from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import LoadingIndicatorComponent from "./common/loader/loading-indicator-component";
import AppNotificationComponent from "./common/notification/app-notification-component";
import ChooseService from "./choose-service-slot/ChooseService";
import {Route, Switch} from "react-router";
import ChooseSlot from "./choose-service-slot/ChooseSlot";
import {BrowserRouter as Router} from "react-router-dom";
import MakePaymentContainer from "./payment/PaymentContainer";

import {Elements} from "@stripe/react-stripe-js";
import {loadStripe} from "@stripe/stripe-js";
import VerifyUser from "./admin/VerifyUser";


const stripePromise = loadStripe("pk_test_PwSfk3SC2VpVYmqjooO9O1Gu00H7iBCCpa");

function App() {
  return (
      <Router>
      <div>
<LoadingIndicatorComponent></LoadingIndicatorComponent>
        <nav className="navbar navbar-expand-md navbar-dark bg-dark fixed-top">
          <a className="navbar-brand" href="/">Ar Salon & Day Spa</a>
        </nav>
        <main role="main" className="container">
          <div className="padding-container">

              <Switch>
                  <Route exact path="/" component={ChooseService}>
                  </Route>
                  <Route path="/chooseslot/:serviceId/:serviceName"  component={ChooseSlot}>
                  </Route>

                  <Route path="/makepayment/:slotId/:serviceId/:serviceName" >
                      <Elements stripe={stripePromise}>
                          <MakePaymentContainer></MakePaymentContainer>
                      </Elements>
                  </Route>
                  <Route path="/admin/verifyuser"  component={VerifyUser}>
                  </Route>

                  <Route>
                      <ChooseService />
                  </Route>
              </Switch>


          </div>
        </main>
          <AppNotificationComponent></AppNotificationComponent>
      </div></Router>
  );
}

export default App;
