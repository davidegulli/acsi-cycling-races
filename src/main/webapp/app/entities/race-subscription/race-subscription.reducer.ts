import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRaceSubscription, defaultValue } from 'app/shared/model/race-subscription.model';

export const ACTION_TYPES = {
  SEARCH_RACESUBSCRIPTIONS: 'raceSubscription/SEARCH_RACESUBSCRIPTIONS',
  FETCH_RACESUBSCRIPTION_LIST: 'raceSubscription/FETCH_RACESUBSCRIPTION_LIST',
  FETCH_RACESUBSCRIPTION: 'raceSubscription/FETCH_RACESUBSCRIPTION',
  FETCH_TEAMS_SUGGESTIONS: 'raceSubscription/FETCH_TEAMS_SUGGESTIONS',
  CLEAR_TEAMS_SUGGESTIONS: 'raceSubscription/CLEAR_TEAMS_SUGGESTIONS',
  CREATE_RACESUBSCRIPTION: 'raceSubscription/CREATE_RACESUBSCRIPTION',
  UPDATE_RACESUBSCRIPTION: 'raceSubscription/UPDATE_RACESUBSCRIPTION',
  DELETE_RACESUBSCRIPTION: 'raceSubscription/DELETE_RACESUBSCRIPTION',
  SET_BLOB: 'raceSubscription/SET_BLOB',
  RESET: 'raceSubscription/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRaceSubscription>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
  teams: []
};

export type RaceSubscriptionState = Readonly<typeof initialState>;

// Reducer

export default (state: RaceSubscriptionState = initialState, action): RaceSubscriptionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_RACESUBSCRIPTIONS):
    case REQUEST(ACTION_TYPES.FETCH_RACESUBSCRIPTION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RACESUBSCRIPTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RACESUBSCRIPTION):
    case REQUEST(ACTION_TYPES.UPDATE_RACESUBSCRIPTION):
    case REQUEST(ACTION_TYPES.DELETE_RACESUBSCRIPTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_RACESUBSCRIPTIONS):
    case FAILURE(ACTION_TYPES.FETCH_RACESUBSCRIPTION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RACESUBSCRIPTION):
    case FAILURE(ACTION_TYPES.CREATE_RACESUBSCRIPTION):
    case FAILURE(ACTION_TYPES.UPDATE_RACESUBSCRIPTION):
    case FAILURE(ACTION_TYPES.DELETE_RACESUBSCRIPTION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_RACESUBSCRIPTIONS):
    case SUCCESS(ACTION_TYPES.FETCH_RACESUBSCRIPTION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_RACESUBSCRIPTION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RACESUBSCRIPTION):
    case SUCCESS(ACTION_TYPES.UPDATE_RACESUBSCRIPTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RACESUBSCRIPTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case SUCCESS(ACTION_TYPES.FETCH_TEAMS_SUGGESTIONS):
      return {
        ...state,
        teams: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CLEAR_TEAMS_SUGGESTIONS):
      return {
        ...state,
        teams: action.payload.data
      };
    case ACTION_TYPES.SET_BLOB:
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name + 'File']: data,
          [name + 'ContentType']: contentType,
          [name + 'FileName']: name
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

const apiUrl = 'api/race-subscriptions';
const apiSearchUrl = 'api/_search/race-subscriptions';

// Actions

export const getSearchEntities: ICrudSearchAction<IRaceSubscription> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_RACESUBSCRIPTIONS,
  payload: axios.get<IRaceSubscription>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`)
});

export const getEntities = (raceId, page?, size?, sort?) => {
  const requestUrl = `${apiUrl}/race/${raceId}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_RACESUBSCRIPTION_LIST,
    payload: axios.get<IRaceSubscription>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IRaceSubscription> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RACESUBSCRIPTION,
    payload: axios.get<IRaceSubscription>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRaceSubscription> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RACESUBSCRIPTION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities(entity.raceId));
  return result;
};

export const updateEntity: ICrudPutAction<IRaceSubscription> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RACESUBSCRIPTION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities(entity.raceId));
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRaceSubscription> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RACESUBSCRIPTION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities(id));
  return result;
};

export const getTeamsSuggestions = code => async dispatch => {
  const requestUrl = `${apiUrl}/teams/code/${code}`;
  const result = dispatch({
    type: ACTION_TYPES.FETCH_TEAMS_SUGGESTIONS,
    payload: axios.get(requestUrl)
  });
  return result;
};

export const clearTeamsSuggestions = () => ({
  type: ACTION_TYPES.FETCH_TEAMS_SUGGESTIONS,
  payload: []
});

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
