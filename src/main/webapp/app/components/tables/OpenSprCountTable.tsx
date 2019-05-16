import React from 'react';

import { Table, Container, Row, Col } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { TextFormat } from 'react-jhipster';
import { connect } from 'react-redux';
import { getAllOpenSprs } from 'app/entities/spr/spr.reducer';

export interface ISprCountTableProp extends StateProps, DispatchProps {}

export interface ISprCountTableState {
  seconds: number;
}

export class OpenSprCountTable extends React.Component<ISprCountTableProp, ISprCountTableState> {
  private interval: number;

  state: ISprCountTableState = {
    seconds: 0
  };

  componentDidMount() {
    this.props.getAllOpenSprs();
    this.interval = window.setInterval(() => this.tick(), 10000);
  }

  componentWillUnmount() {
    clearInterval(this.interval);
  }

  // We will refresh the components every 5 seconds
  tick() {
    this.setState(prevState => ({
      seconds: prevState.seconds + 1
    }));
    this.props.getAllOpenSprs();
  }

  render() {
    const { openSprEntites } = this.props;
    return (
      <>
        <div className="table-responsive">
          <Table responsive>
            <thead>
            <tr>
              <th>SPR</th>
              <th>Priority</th>
              <th>User</th>
            </tr>
            </thead>
            <tbody>
            {openSprEntites === undefined
              ? (
                <Container className="text-center">
                  <h6>Hmm. Looks like there are no sprs.</h6>
                </Container>
              )
              : (openSprEntites.map((spr, key) => (
                <tr key={`entity-${key}`}>
                  <td>{spr.fullNumber}</td>
                  <td>{spr.priority}</td>
                  <td>{spr.user ? spr.user.login : ''}</td>
                </tr>
              )))
            }
            </tbody>
          </Table>
        </div>
        </>
    );
  }
}

const mapStateToProps = ({ spr }) => ({
  openSprEntites: spr.openSprEntites
});

const mapDispatchToProps = {
  getAllOpenSprs
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OpenSprCountTable);
