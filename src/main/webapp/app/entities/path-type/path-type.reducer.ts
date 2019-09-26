import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPathType, defaultValue } from 'app/shared/model/path-type.model';

export const ACTION_TYPES = {
  SEARCH_PATHTYPES: 'pathType/SEARCH_PATHTYPES',
  FETCH_PATHTYPE_LIST: 'pathType/FETCH_PATHTYPE_LIST',
  FETCH_PATHTYPE_LIST_BY_RACE: 'pathType/FETCH_PATHTYPE_LIST_BY_RACE',
  FETCH_PATHTYPE: 'pathType/FETCH_PATHTYPE',
  CREATE_PATHTYPE: 'pathType/CREATE_PATHTYPE',
  UPDATE_PATHTYPE: 'pathType/UPDATE_PATHTYPE',
  DELETE_PATHTYPE: 'pathType/DELETE_PATHTYPE',
  RESET: 'pathType/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPathType>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type PathTypeState = Readonly<typeof initialState>;

// Reducer

export default (state: PathTypeState = initialState, action): PathTypeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_PATHTYPES):
    case REQUEST(ACTION_TYPES.FETCH_PATHTYPE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PATHTYPE_LIST_BY_RACE):
    case REQUEST(ACTION_TYPES.FETCH_PATHTYPE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PATHTYPE):
    case REQUEST(ACTION_TYPES.UPDATE_PATHTYPE):
    case REQUEST(ACTION_TYPES.DELETE_PATHTYPE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_PATHTYPES):
    case FAILURE(ACTION_TYPES.FETCH_PATHTYPE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PATHTYPE_LIST_BY_RACE):
    case FAILURE(ACTION_TYPES.FETCH_PATHTYPE):
    case FAILURE(ACTION_TYPES.CREATE_PATHTYPE):
    case FAILURE(ACTION_TYPES.UPDATE_PATHTYPE):
    case FAILURE(ACTION_TYPES.DELETE_PATHTYPE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_PATHTYPES):
    case SUCCESS(ACTION_TYPES.FETCH_PATHTYPE_LIST):
    case SUCCESS(ACTION_TYPES.FETCH_PATHTYPE_LIST_BY_RACE):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PATHTYPE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PATHTYPE):
    case SUCCESS(ACTION_TYPES.UPDATE_PATHTYPE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PATHTYPE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/path-types';
const apiSearchUrl = 'api/_search/path-types';

// Actions

export const getSearchEntities: ICrudSearchAction<IPathType> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_PATHTYPES,
  payload: axios.get<IPathType>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IPathType> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PATHTYPE_LIST,
  payload: axios.get<IPathType>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntitiesByRace = raceId => {
  const requestUrl = `${apiUrl}/race/${raceId}`;
  return {
    type: ACTION_TYPES.FETCH_PATHTYPE_LIST_BY_RACE,
    payload: axios.get<IPathType>(requestUrl)
  };
};

export const getEntity: ICrudGetAction<IPathType> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PATHTYPE,
    payload: axios.get<IPathType>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPathType> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PATHTYPE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPathType> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PATHTYPE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPathType> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PATHTYPE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
