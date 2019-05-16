import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISpr, defaultValue } from 'app/shared/model/spr.model';

export const ACTION_TYPES = {
  SEARCH_SPRS: 'spr/SEARCH_SPRS',
  FETCH_SPR_LIST: 'spr/FETCH_SPR_LIST',
  FETCH_OPEN_SPR_LIST: 'spr/FETCH_OPEN_SPR_LIST',
  FETCH_REVIEWED_SPR_LIST: 'spr/FETCH_REVIEWED_SPR_LIST',
  FETCH_SPR: 'spr/FETCH_SPR',
  CREATE_SPR: 'spr/CREATE_SPR',
  UPDATE_SPR: 'spr/UPDATE_SPR',
  DELETE_SPR: 'spr/DELETE_SPR',
  SET_BLOB: 'spr/SET_BLOB',
  RESET: 'spr/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISpr>,
  openSprEntites: [] as ReadonlyArray<ISpr>,
  reviewedSprEntities: [] as ReadonlyArray<ISpr>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type SprState = Readonly<typeof initialState>;

// Reducer

export default (state: SprState = initialState, action): SprState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_SPRS):
    case REQUEST(ACTION_TYPES.FETCH_SPR_LIST):
    case REQUEST(ACTION_TYPES.FETCH_OPEN_SPR_LIST):
    case REQUEST(ACTION_TYPES.FETCH_REVIEWED_SPR_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SPR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SPR):
    case REQUEST(ACTION_TYPES.UPDATE_SPR):
    case REQUEST(ACTION_TYPES.DELETE_SPR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_SPRS):
    case FAILURE(ACTION_TYPES.FETCH_SPR_LIST):
    case FAILURE(ACTION_TYPES.FETCH_OPEN_SPR_LIST):
    case FAILURE(ACTION_TYPES.FETCH_REVIEWED_SPR_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SPR):
    case FAILURE(ACTION_TYPES.CREATE_SPR):
    case FAILURE(ACTION_TYPES.UPDATE_SPR):
    case FAILURE(ACTION_TYPES.DELETE_SPR):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SPRS):
    case SUCCESS(ACTION_TYPES.FETCH_SPR_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_OPEN_SPR_LIST):
      return {
        ...state,
        loading: false,
        openSprEntites: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_REVIEWED_SPR_LIST):
      return {
        ...state,
        loading: false,
        reviewedSprEntities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SPR):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SPR):
    case SUCCESS(ACTION_TYPES.UPDATE_SPR):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SPR):
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

const apiUrl = 'api/sprs';
const apiSearchUrl = 'api/_search/sprs';
const apiOpenUrl = 'api/sprs/open';
const apiReviewedUrl = 'api/sprs/reviewed';

// Actions

export const getSearchEntities: ICrudSearchAction<ISpr> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_SPRS,
  payload: axios.get<ISpr>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<ISpr> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SPR_LIST,
  payload: axios.get<ISpr>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getAllOpenSprs: ICrudGetAllAction<ISpr> = () => ({
  type: ACTION_TYPES.FETCH_OPEN_SPR_LIST,
  payload: axios.get<ISpr>(apiOpenUrl)
});

export const getReviewedSprs: ICrudGetAllAction<ISpr> = () => ({
  type: ACTION_TYPES.FETCH_REVIEWED_SPR_LIST,
  payload: axios.get<ISpr>(apiReviewedUrl)
});

export const getEntity: ICrudGetAction<ISpr> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SPR,
    payload: axios.get<ISpr>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISpr> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SPR,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISpr> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SPR,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISpr> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SPR,
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
