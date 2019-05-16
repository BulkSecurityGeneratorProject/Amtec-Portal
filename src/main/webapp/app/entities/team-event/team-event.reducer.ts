import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITeamEvent, defaultValue } from 'app/shared/model/team-event.model';

export const ACTION_TYPES = {
  SEARCH_TEAMEVENTS: 'teamEvent/SEARCH_TEAMEVENTS',
  FETCH_TEAMEVENT_LIST: 'teamEvent/FETCH_TEAMEVENT_LIST',
  FETCH_TEAMEVENT: 'teamEvent/FETCH_TEAMEVENT',
  CREATE_TEAMEVENT: 'teamEvent/CREATE_TEAMEVENT',
  UPDATE_TEAMEVENT: 'teamEvent/UPDATE_TEAMEVENT',
  DELETE_TEAMEVENT: 'teamEvent/DELETE_TEAMEVENT',
  SET_BLOB: 'teamEvent/SET_BLOB',
  RESET: 'teamEvent/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITeamEvent>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type TeamEventState = Readonly<typeof initialState>;

// Reducer

export default (state: TeamEventState = initialState, action): TeamEventState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_TEAMEVENTS):
    case REQUEST(ACTION_TYPES.FETCH_TEAMEVENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TEAMEVENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_TEAMEVENT):
    case REQUEST(ACTION_TYPES.UPDATE_TEAMEVENT):
    case REQUEST(ACTION_TYPES.DELETE_TEAMEVENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_TEAMEVENTS):
    case FAILURE(ACTION_TYPES.FETCH_TEAMEVENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TEAMEVENT):
    case FAILURE(ACTION_TYPES.CREATE_TEAMEVENT):
    case FAILURE(ACTION_TYPES.UPDATE_TEAMEVENT):
    case FAILURE(ACTION_TYPES.DELETE_TEAMEVENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_TEAMEVENTS):
    case SUCCESS(ACTION_TYPES.FETCH_TEAMEVENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_TEAMEVENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_TEAMEVENT):
    case SUCCESS(ACTION_TYPES.UPDATE_TEAMEVENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_TEAMEVENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.SET_BLOB:
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType
        }
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/team-events';
const apiSearchUrl = 'api/_search/team-events';

// Actions

export const getSearchEntities: ICrudSearchAction<ITeamEvent> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_TEAMEVENTS,
  payload: axios.get<ITeamEvent>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<ITeamEvent> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_TEAMEVENT_LIST,
  payload: axios.get<ITeamEvent>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ITeamEvent> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TEAMEVENT,
    payload: axios.get<ITeamEvent>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ITeamEvent> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TEAMEVENT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITeamEvent> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TEAMEVENT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITeamEvent> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TEAMEVENT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType
  }
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
