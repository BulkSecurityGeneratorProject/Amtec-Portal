import axios from 'axios';
import { ICrudGetAllAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRelease, defaultValue } from 'app/shared/model/release.model';

export const ACTION_TYPES = {
  FETCH_CURRENT_RELEASE_LIST: 'release/FETCH_CURRENT_RELEASE_LIST',
  FETCH_FUTURE_RELEASE_LIST: 'release/FETCH_FUTURE_RELEASE_LIST'
};

const initialState = {
  loading: false,
  currentEntities: {
    entities: [] as ReadonlyArray<IRelease>
  },
  futureEntities: {
    entities: [] as ReadonlyArray<IRelease>
  }
};

export type HomeState = Readonly<typeof initialState>;

// Reducer

export default (state: HomeState = initialState, action): HomeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FUTURE_RELEASE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CURRENT_RELEASE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FUTURE_RELEASE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CURRENT_RELEASE_LIST):
    case SUCCESS(ACTION_TYPES.FETCH_FUTURE_RELEASE_LIST):
      return {
        ...state,
        loading: false,
        futureEntities: {
          entities: action.payload.data
        }
      };
    case SUCCESS(ACTION_TYPES.FETCH_CURRENT_RELEASE_LIST):
      return {
        ...state,
        loading: false,
        currentEntities: {
          entities: action.payload.data
        }
      };
    default:
      return state;
  }
};

const apiUrlCurrent = 'api/releases/current';
const apiUrlFuture = 'api/releases/future';

// Actions

export const getCurrentReleases: ICrudGetAllAction<IRelease> = () => ({
  type: ACTION_TYPES.FETCH_CURRENT_RELEASE_LIST,
  payload: axios.get<IRelease>(apiUrlCurrent)
});

export const getFutureReleases: ICrudGetAllAction<IRelease> = () => ({
  type: ACTION_TYPES.FETCH_CURRENT_RELEASE_LIST,
  payload: axios.get<IRelease>(apiUrlFuture)
});
