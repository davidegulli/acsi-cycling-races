import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { INonAcsiTeam, defaultValue } from 'app/shared/model/non-acsi-team.model';

export const ACTION_TYPES = {
  SEARCH_NONACSITEAMS: 'nonAcsiTeam/SEARCH_NONACSITEAMS',
  FETCH_NONACSITEAM_LIST: 'nonAcsiTeam/FETCH_NONACSITEAM_LIST',
  FETCH_NONACSITEAM: 'nonAcsiTeam/FETCH_NONACSITEAM',
  CREATE_NONACSITEAM: 'nonAcsiTeam/CREATE_NONACSITEAM',
  UPDATE_NONACSITEAM: 'nonAcsiTeam/UPDATE_NONACSITEAM',
  DELETE_NONACSITEAM: 'nonAcsiTeam/DELETE_NONACSITEAM',
  RESET: 'nonAcsiTeam/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<INonAcsiTeam>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type NonAcsiTeamState = Readonly<typeof initialState>;

// Reducer

export default (state: NonAcsiTeamState = initialState, action): NonAcsiTeamState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_NONACSITEAMS):
    case REQUEST(ACTION_TYPES.FETCH_NONACSITEAM_LIST):
    case REQUEST(ACTION_TYPES.FETCH_NONACSITEAM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_NONACSITEAM):
    case REQUEST(ACTION_TYPES.UPDATE_NONACSITEAM):
    case REQUEST(ACTION_TYPES.DELETE_NONACSITEAM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_NONACSITEAMS):
    case FAILURE(ACTION_TYPES.FETCH_NONACSITEAM_LIST):
    case FAILURE(ACTION_TYPES.FETCH_NONACSITEAM):
    case FAILURE(ACTION_TYPES.CREATE_NONACSITEAM):
    case FAILURE(ACTION_TYPES.UPDATE_NONACSITEAM):
    case FAILURE(ACTION_TYPES.DELETE_NONACSITEAM):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_NONACSITEAMS):
    case SUCCESS(ACTION_TYPES.FETCH_NONACSITEAM_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_NONACSITEAM):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_NONACSITEAM):
    case SUCCESS(ACTION_TYPES.UPDATE_NONACSITEAM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_NONACSITEAM):
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

const apiUrl = 'api/non-acsi-teams';
const apiSearchUrl = 'api/_search/non-acsi-teams';

// Actions

export const getSearchEntities: ICrudSearchAction<INonAcsiTeam> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_NONACSITEAMS,
  payload: axios.get<INonAcsiTeam>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`)
});

export const getEntities: ICrudGetAllAction<INonAcsiTeam> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_NONACSITEAM_LIST,
    payload: axios.get<INonAcsiTeam>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<INonAcsiTeam> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_NONACSITEAM,
    payload: axios.get<INonAcsiTeam>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<INonAcsiTeam> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_NONACSITEAM,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<INonAcsiTeam> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_NONACSITEAM,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<INonAcsiTeam> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_NONACSITEAM,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
