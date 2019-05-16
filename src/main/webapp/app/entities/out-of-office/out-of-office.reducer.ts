import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IOutOfOffice, defaultValue } from 'app/shared/model/out-of-office.model';

export const ACTION_TYPES = {
  SEARCH_OUTOFOFFICES: 'outOfOffice/SEARCH_OUTOFOFFICES',
  FETCH_OUTOFOFFICE_LIST: 'outOfOffice/FETCH_OUTOFOFFICE_LIST',
  FETCH_OUTOFOFFICE: 'outOfOffice/FETCH_OUTOFOFFICE',
  CREATE_OUTOFOFFICE: 'outOfOffice/CREATE_OUTOFOFFICE',
  UPDATE_OUTOFOFFICE: 'outOfOffice/UPDATE_OUTOFOFFICE',
  DELETE_OUTOFOFFICE: 'outOfOffice/DELETE_OUTOFOFFICE',
  SET_BLOB: 'outOfOffice/SET_BLOB',
  RESET: 'outOfOffice/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IOutOfOffice>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type OutOfOfficeState = Readonly<typeof initialState>;

// Reducer

export default (state: OutOfOfficeState = initialState, action): OutOfOfficeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_OUTOFOFFICES):
    case REQUEST(ACTION_TYPES.FETCH_OUTOFOFFICE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_OUTOFOFFICE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_OUTOFOFFICE):
    case REQUEST(ACTION_TYPES.UPDATE_OUTOFOFFICE):
    case REQUEST(ACTION_TYPES.DELETE_OUTOFOFFICE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_OUTOFOFFICES):
    case FAILURE(ACTION_TYPES.FETCH_OUTOFOFFICE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_OUTOFOFFICE):
    case FAILURE(ACTION_TYPES.CREATE_OUTOFOFFICE):
    case FAILURE(ACTION_TYPES.UPDATE_OUTOFOFFICE):
    case FAILURE(ACTION_TYPES.DELETE_OUTOFOFFICE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_OUTOFOFFICES):
    case SUCCESS(ACTION_TYPES.FETCH_OUTOFOFFICE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_OUTOFOFFICE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_OUTOFOFFICE):
    case SUCCESS(ACTION_TYPES.UPDATE_OUTOFOFFICE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_OUTOFOFFICE):
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

const apiUrl = 'api/out-of-offices';
const apiSearchUrl = 'api/_search/out-of-offices';

// Actions

export const getSearchEntities: ICrudSearchAction<IOutOfOffice> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_OUTOFOFFICES,
  payload: axios.get<IOutOfOffice>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IOutOfOffice> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_OUTOFOFFICE_LIST,
  payload: axios.get<IOutOfOffice>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IOutOfOffice> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_OUTOFOFFICE,
    payload: axios.get<IOutOfOffice>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IOutOfOffice> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_OUTOFOFFICE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IOutOfOffice> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_OUTOFOFFICE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IOutOfOffice> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_OUTOFOFFICE,
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
