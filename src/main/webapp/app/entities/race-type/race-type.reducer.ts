import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRaceType, defaultValue } from 'app/shared/model/race-type.model';

export const ACTION_TYPES = {
  SEARCH_RACETYPES: 'raceType/SEARCH_RACETYPES',
  FETCH_RACETYPE_LIST: 'raceType/FETCH_RACETYPE_LIST',
  FETCH_RACETYPE: 'raceType/FETCH_RACETYPE',
  CREATE_RACETYPE: 'raceType/CREATE_RACETYPE',
  UPDATE_RACETYPE: 'raceType/UPDATE_RACETYPE',
  DELETE_RACETYPE: 'raceType/DELETE_RACETYPE',
  RESET: 'raceType/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRaceType>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type RaceTypeState = Readonly<typeof initialState>;

// Reducer

export default (state: RaceTypeState = initialState, action): RaceTypeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_RACETYPES):
    case REQUEST(ACTION_TYPES.FETCH_RACETYPE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RACETYPE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RACETYPE):
    case REQUEST(ACTION_TYPES.UPDATE_RACETYPE):
    case REQUEST(ACTION_TYPES.DELETE_RACETYPE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_RACETYPES):
    case FAILURE(ACTION_TYPES.FETCH_RACETYPE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RACETYPE):
    case FAILURE(ACTION_TYPES.CREATE_RACETYPE):
    case FAILURE(ACTION_TYPES.UPDATE_RACETYPE):
    case FAILURE(ACTION_TYPES.DELETE_RACETYPE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_RACETYPES):
    case SUCCESS(ACTION_TYPES.FETCH_RACETYPE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_RACETYPE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RACETYPE):
    case SUCCESS(ACTION_TYPES.UPDATE_RACETYPE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RACETYPE):
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

const apiUrl = 'api/race-types';
const apiSearchUrl = 'api/_search/race-types';

// Actions

export const getSearchEntities: ICrudSearchAction<IRaceType> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_RACETYPES,
  payload: axios.get<IRaceType>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IRaceType> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_RACETYPE_LIST,
  payload: axios.get<IRaceType>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IRaceType> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RACETYPE,
    payload: axios.get<IRaceType>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRaceType> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RACETYPE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRaceType> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RACETYPE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRaceType> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RACETYPE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
