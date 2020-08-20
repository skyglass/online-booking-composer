
import React, {Component, Fragment} from 'react';
import { loadingIndicator } from './loading-indicator';
import {ProgressBar} from "react-bootstrap";

class LoadingIndicatorComponent extends Component {



    constructor(props) {
        super(props);
        this.state = {
            "showLoading": false
        };
    }

    componentDidMount() {

        this.subscription =  loadingIndicator.onChange().subscribe(value => {

            this.setState({
                "showLoading": value
            })
        })
    }

    componentWillUnmount() {
        this.subscription.unsubscribe();
    }

    render() {

        const {showLoading} = this.state

                return <Fragment>

                    { showLoading &&  <div className="progress-container"><ProgressBar animated now={100} /></div>}
                </Fragment>

    }
}


export default LoadingIndicatorComponent;
